from dependency_injector import containers, providers
from pymongo import MongoClient
from redis import Redis
from confluent_kafka import Consumer, Producer
import logging
import os

from services.cache import Cache
from services.fx_storage import FxStorage
from services.storage import Storage
from services.image import ImageService
from services.fx_nsfw_detector_service import FxNsfwDetectorService


def create_kafka_consumer(servers, group_id, auto_offset_reset) -> Consumer:
    config = {
        'bootstrap.servers': servers,
        'group.id': group_id,
        'auto.offset.reset': auto_offset_reset
    }
    return Consumer(config)


def create_kafka_producer(servers, group_id, auto_offset_reset) -> Producer:
    config = {
        'bootstrap.servers': servers,
        'group.id': group_id,
        'auto.offset.reset': auto_offset_reset
    }
    return Producer(config)


def create_logger():
    logger = logging.getLogger("Pre-Processing-Service")
    logger.setLevel(logging.DEBUG)
    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.DEBUG)
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    console_handler.setFormatter(formatter)
    logger.addHandler(console_handler)
    return logger


def create_mongo_db_client(host, port, username, password, db):
    uri = f"mongodb://{username}:{password}@{host}/{db}?authMechanism=PLAIN"
    return MongoClient(uri)


def create_container():
    class Container(containers.DeclarativeContainer):
        _root_dir_path = os.path.dirname(os.path.abspath(__file__))
        _config_files = [os.path.join(_root_dir_path, 'config.yaml')]
        if os.getenv("PROD_ENV") != 'true':
            _config_files.append(os.path.join(_root_dir_path, 'config-dev.yaml'))
        config = providers.Configuration(yaml_files=_config_files)
        wiring_config = containers.WiringConfiguration(modules=[])

        redis_service = providers.Factory(
            Redis,
            host=config.redis.host,
            port=config.redis.port,
            password=config.redis.password,
            db=config.redis.db,
        )

        kafka_consumer_service = providers.Factory(
            create_kafka_consumer,
            servers=config.kafka.bootstrap.servers,
            group_id=config.kafka.group.id,
            auto_offset_reset=config.kafka.auto.offset.reset,

        )
        kafka_producer_service = providers.Factory(
            create_kafka_producer,
            servers=config.kafka.bootstrap.servers,
            group_id=config.kafka.group.id,
            auto_offset_reset=config.kafka.auto.offset.reset,

        )

        logger_service = providers.Factory(
            create_logger
        )

        storage_service = providers.Factory(
            Storage,
            root_dir=config.storage.root_dir,
            logger=logger_service,
        )

        image_caching_service = providers.Factory(
            Cache,
            scope="images",
            storage_service=storage_service
        )

        fx_nsfw_detector = providers.Factory(
            FxNsfwDetectorService,
            host=config.fx_nsfw_detector.host,
            port=config.fx_nsfw_detector.port
        )

        image_service = providers.Factory(
            ImageService,
            logger=logger_service,
            storage_service=storage_service,
            fx_nsfw=fx_nsfw_detector

        )

        fx_storage = providers.Factory(
            FxStorage,
            host=config.fx_storage.host,
            port=config.fx_storage.port,
            ssl=config.fx_storage.ssl,
            username=config.fx_storage.username,
            password=config.fx_storage.password,
        )

        mongo_client = providers.Factory(
            create_mongo_db_client,
            host=config.ferret_db.host,
            port=config.ferret_db.port,
            username=config.ferret_db.username,
            password=config.ferret_db.password,
            db=config.ferret_db.db_name,
        )

    return Container()


root_container = create_container()
