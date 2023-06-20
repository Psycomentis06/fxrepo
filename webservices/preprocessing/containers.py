from dependency_injector import containers, providers
from redis import Redis


class Container(containers.DeclarativeContainer):
    config = providers.Configuration(yaml_files=['config.yaml', 'config-dev.yaml'])

    wiring_config = containers.WiringConfiguration(modules=["views.image"])

    redis_service = providers.Factory(
        Redis,
        host=config.redis.host,
        port=config.redis.port,
        password=config.redis.password,
        db=config.redis.db,
    )
