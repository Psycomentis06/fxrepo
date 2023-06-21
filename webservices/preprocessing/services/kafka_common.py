from enum import Enum
from typing import TypedDict, List


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


class KafkaData(TypedDict):
    user_id: str
    post_id: str
    action: Actions
    target: List[Targets]
    status: Status
