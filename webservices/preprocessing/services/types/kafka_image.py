import datetime
from dataclasses import dataclass
from typing import TypedDict, List
from .kafka_post import Tag, Category


@dataclass
class ImageVariantData(TypedDict):
    id: int
    original: bool
    width: int
    height: int
    size: int
    title: str
    url: str
    md5: str
    sha256: str


@dataclass
class ImageData(TypedDict):
    id: str
    placement: str
    orphan: bool
    variants: List[ImageVariantData]
    accentColor: str
    colorPalette: str
    landscape: str


@dataclass
class ImagePostData(TypedDict):
    id: str
    slug: str
    title: str
    content: str
    createdAt: datetime.datetime
    updatedAt: datetime.datetime
    postType: str
    userId: str
    publik: str
    ready: bool
    nsfw: bool
    tags: List[Tag]
    category: Category
    thumbnail: str
    image: ImageData
