from dependency_injector import containers, providers
from services import redis
import os


class Container(containers.DeclarativeContainer):
    config = providers.Configuration(yaml_files=['config.yaml', 'config-dev.yaml'])

    wiring_config = containers.WiringConfiguration(modules=["views.image"])

    redis_service = providers.Factory(
        redis.RedisService,
        redis=config.redis.endpoint
    )