import logging
import os
from enum import StrEnum
import pathlib


class FileType(StrEnum):
    IMAGE = "image"
    VIDEO = "video"
    AUDIO = "audio"


class Storage:
    IMAGE_DIR = "images"

    def __init__(self, root_dir: str, logger: logging.Logger):
        self.logger = logger
        try:
            os.stat(root_dir)
            logger.info(f"Storage root directory already exists: {root_dir}")
        except FileNotFoundError:
            pathlib.Path(root_dir).mkdir(parents=True, exist_ok=True)
            logger.info(f"Storage root directory created: {root_dir}")
        finally:
            pathlib.Path(root_dir + "/" + self.IMAGE_DIR).mkdir(parents=True, exist_ok=True)
        self.root_dir = root_dir

    def store(self, name: str, file: bytes, f_type: FileType):
        file_dir = ""
        if f_type == FileType.IMAGE:
            file_dir = self.IMAGE_DIR + "/"
        pathlib.Path(self.root_dir + "/" + file_dir + name).write_bytes(file)
        self.logger.info(f"File {name} stored in {file_dir}")

    def load(self, name: str, f_type: FileType) -> bytes | None:
        file_dir = ""
        if f_type == FileType.IMAGE:
            file_dir = self.IMAGE_DIR + "/"
        try:
            file = pathlib.Path(self.root_dir + "/" + file_dir + name).read_bytes()
            self.logger.info(f"File {name} loaded from {file_dir}")
            return file
        except FileNotFoundError:
            self.logger.info(f"File {name} not found in {file_dir}")
        return None

    def delete(self, name: str, f_type: FileType) -> bool:
        file_dir = ""
        if f_type == FileType.IMAGE:
            file_dir = self.IMAGE_DIR + "/"
        try:
            pathlib.Path(self.root_dir + "/" + file_dir + name).unlink()
            self.logger.info(f"File {name} deleted from {file_dir}")
            return True
        except FileNotFoundError:
            self.logger.info(f"File {name} not found in {file_dir}")
            return False
