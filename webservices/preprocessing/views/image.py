from dependency_injector.wiring import inject, Provide
from services import redis
from containers import Container


@inject
def index(redis_service: redis.RedisService = Provide[Container.redis_service]):
    s = redis_service.getRedis()
    if len(s) > 0:
        return s
    return "Hello from index"


def upload():
    return "upload image"


def remove():
    return "Remove img"


def get():
    return "Get img"
