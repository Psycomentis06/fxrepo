from dependency_injector import containers, providers
from redis import Redis
from confluent_kafka import Consumer, Producer
import logging
import os
from services.storage import Storage


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


def create_container():
    class Container(containers.DeclarativeContainer):
        _config_files = ['config.yaml']
        if os.getenv("DEV_ENV") == 'true':
            _config_files.append('config-dev.yaml')
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

    return Container()


root_container = create_container()
