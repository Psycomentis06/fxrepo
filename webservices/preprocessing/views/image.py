from dependency_injector.wiring import inject, Provide
from redis import Redis
from containers import Container


@inject
def index(redis_service: Redis = Provide[Container.redis_service]):
    redis_service.set("foo1", "bar1")
    redis_service.set("foo2", "bar2")
    redis_service.set("foo3", "bar3")
    redis_service.set("foo4", "bar4")
    return redis_service.get("foo3")


def upload():
    return "upload image"


def remove():
    return "Remove img"


def get():
    return "Get img"
