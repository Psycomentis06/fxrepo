from typing import TypedDict, List
from .kafka_common import KafkaData


class ImageVariantData(TypedDict):
    dimensions: List[int, int]
    url: str


class ImageData(TypedDict):
    accent_color: str
    color_palette: str
    dimension: List[int, int]
    is_nsfw: bool
    type: str
    url: str
    thumbnail: str
    name: str
    size: int
    variants: List[ImageVariantData]