import copy
import pathlib
import urllib.request
from PIL import Image
import uuid
import storage
import logging


class ImageService:
    def __init__(self, logger: logging.Logger, storage_service: storage.Storage):
        self.logger = logger
        self.storage_service = storage_service

    def is_png(self, img: Image.Image) -> bool:
        return self.guess_img_type(img) == "PNG"

    def guess_img_type(self, img: Image.Image) -> str:
        return img.format

    def create_thumbnail(self, img: Image.Image, filepath: str) -> bool:
        n_img = copy.copy(img)
        n_img.thumbnail((54, 54))
        try:
            n_img.save(filepath + "-thumb", "JPEG")
            self.logger.info("Thumbnail created: {}".format(filepath))
            return True
        except OSError:
            self.logger.error("Could not create thumbnail: {}".format(filepath))
            return False

    def get_local_img(self, name: str) -> bytes | None:
        try:
            return pathlib.Path(self.storage_service.get_image_dir_path() + name).read_bytes()
        except FileNotFoundError:
            return None

    def save_remote_image(self, url: str, image_name: str):
        image_content = bytes()
        with urllib.request.urlopen(url) as response:
            image_content = response.read()
        self.storage_service.store(image_name, image_content, storage.FileType.IMAGE)

    def get_image(self, url):
        pass

    def get_color_palette(self, img):
        pass

    def get_accent_color(self, img):
        pass

    def is_nsfw(self, img):
        pass

    def reformat_img(self, img):
        pass

    def compress_img(self, img):
        pass

    def create_variants(self, img):
        pass
