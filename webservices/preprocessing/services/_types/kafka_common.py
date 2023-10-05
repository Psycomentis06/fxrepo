import datetime
from dataclasses import dataclass
from enum import Enum
from typing import TypedDict, List, Generic, TypeVar

T = TypeVar('T')


class Actions(Enum):
    ADD = 'ADD'
    REMOVE = 'REMOVE'
    UPDATE = 'UPDATE'


class Targets(Enum):
    PRE_PROCESSING = 'PRE_PROCESSING'
    STORAGE = 'STORAGE'
    MAIN = 'MAIN'


class Status(Enum):
    DONE = 'DONE'
    CANCELED = 'CANCELED'
    FAILED = 'FAILED'
    IN_PROGRESS = 'IN_PROGRESS'


@dataclass
class KafkaData(TypedDict, Generic[T]):
    eventId: str
    eventTime: datetime.datetime
    action: Actions
    targets: List[Targets]
    status: Status
    payload: T
