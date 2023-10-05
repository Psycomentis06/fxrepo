from typing import TypedDict
from dataclasses import dataclass


@dataclass
class Tag(TypedDict):
    name: str


@dataclass
class Category(TypedDict):
    id: int
    name: str
    description: str
    svgIcon: str
    thumbnail: str
    bgColor: str
    fgColor: str
    color: str
