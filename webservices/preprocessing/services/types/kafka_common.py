import datetime
from dataclasses import dataclass
from enum import Enum
from typing import TypedDict, List, Generic, TypeVar

T = TypeVar('T')


class Actions(Enum):
    ADD = 'Add'
    REMOVE = 'Remove'
    UPDATE = 'Update'


class Targets(Enum):
    PRE_PROCESSING = 'Pre-Processing'
    STORAGE = 'Storage'
    MAIN = 'Main'


class Status(Enum):
    DONE = 'Done'
    CANCELED = 'Canceled'
    FAILED = 'Failed'
    IN_PROGRESS = 'In-progress'


@dataclass
class KafkaData(TypedDict, Generic[T]):
    eventId: str
    eventTime: datetime.datetime
    action: Actions
    target: List[Targets]
    status: Status
    payload: T
