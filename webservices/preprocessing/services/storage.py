import logging
import os
from enum import StrEnum
import pathlib


class FileType(StrEnum):
    IMAGE = "image"
    VIDEO = "video"
    AUDIO = "audio"
    CACHE = "cache"


class Storage:
    IMAGE_DIR = "images"
    CACHE_DIR = "cache"

    def __init__(self, root_dir: str, logger: logging.Logger):
        self._logger = logger
        try:
            os.stat(root_dir)
            logger.info(f"Storage root directory already exists: {root_dir}")
        except FileNotFoundError:
            pathlib.Path(root_dir).mkdir(parents=True, exist_ok=True)
            logger.info(f"Storage root directory created: {root_dir}")
        finally:
            pathlib.Path(root_dir + "/" + self.IMAGE_DIR).mkdir(parents=True, exist_ok=True)
            pathlib.Path(root_dir + "/" + self.CACHE_DIR).mkdir(parents=True, exist_ok=True)
        self.root_dir = root_dir

    def get_dir_sub_path(self, f_type: FileType):
        file_dir = ""
        if f_type == FileType.IMAGE:
            file_dir = self.IMAGE_DIR + "/"
        elif f_type == FileType.CACHE:
            file_dir = self.CACHE_DIR + "/"
        return self.root_dir + "/" + file_dir

    def get_image_dir_path(self):
        return self.get_dir_sub_path(FileType.IMAGE)

    def store(self, name: str, file: bytes, f_type: FileType) -> bool:
        file_dir = self.get_dir_sub_path(f_type)
        try:
            pathlib.Path(file_dir + name).write_bytes(file)
            self._logger.info(f"File {name} stored in {file_dir}")
            return True
        except FileNotFoundError as e:
            self._logger.error("File {} not stored".format(name))
            return False

    def load(self, name: str, f_type: FileType) -> bytes | None:
        file_dir = self.get_dir_sub_path(f_type)
        try:
            file = pathlib.Path(self.root_dir + "/" + file_dir + name).read_bytes()
            self._logger.info(f"File {name} loaded from {file_dir}")
            return file
        except FileNotFoundError:
            self._logger.info(f"File {name} not found in {file_dir}")
        return None

    def delete(self, name: str, f_type: FileType) -> bool:
        file_dir = self.get_dir_sub_path(f_type)
        try:
            pathlib.Path(file_dir + name).unlink()
            self._logger.info(f"File {name} deleted from {file_dir}")
            return True
        except FileNotFoundError:
            self._logger.info(f"File {name} not found in {file_dir}")
            return False

    def file_exists(self, name, f_type: FileType):
        return pathlib.Path(self.get_dir_sub_path(f_type) + name).exists()
