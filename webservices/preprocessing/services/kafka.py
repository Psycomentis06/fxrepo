import json
from typing import TypeVar, Type
from enum import StrEnum

from .types.kafka_common import KafkaData

T = TypeVar('T')


def decode(val: str, t: Type[KafkaData[T]]) -> KafkaData[T]:
    data = json.loads(val)
    return t(**data)


def encode(data: KafkaData[T]) -> str:
    d = json.dumps(data)
    return d


class KafkaTopics(StrEnum):
    IMAGE = 'fx_repo_topics_image'
