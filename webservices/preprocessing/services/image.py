import copy
import hashlib
import os.path
import numpy as np
from sklearn.cluster import KMeans
from ._types import kafka_image
import pathlib
import urllib.request
from PIL import Image

from .fx_nsfw_detector_service import FxNsfwDetectorService
from .storage import Storage, FileType
import logging


class ImageService:
    def __init__(self, logger: logging.Logger, storage_service: Storage, fx_nsfw: FxNsfwDetectorService):
        self.logger = logger
        self.storage_service = storage_service
        self.fx_nsfw_service = fx_nsfw

    def is_png(self, img: Image.Image) -> bool:
        return self.guess_img_type(img) == "PNG"

    def guess_img_type(self, img: Image.Image) -> str:
        return img.format

    def create_thumbnail(self, img: Image.Image, img_name: str) -> str | None:
        n_img = copy.copy(img)
        n_img.thumbnail((54, 54))
        filepath = self.storage_service.get_image_dir_path() + img_name + "-thumb"
        try:
            n_img.save(filepath, "PNG")
            self.logger.info("Thumbnail created: {}".format(filepath))
            return filepath
        except OSError:
            self.logger.error("Could not create thumbnail: {}".format(filepath))
            return None

    def get_local_img(self, name: str) -> bytes | None:
        try:
            return pathlib.Path(self.storage_service.get_image_dir_path() + name).read_bytes()
        except FileNotFoundError:
            return None

    def save_remote_image(self, url: str, image_name: str):
        main_service_host = os.getenv('MAIN_SERVICE_HOST')
        if main_service_host is None:
            self.logger.warning("MAIN_SERVICE_HOST environment variable is not set.")
        main_service_name = os.getenv('MAIN_SERVICE_NAME')
        if main_service_name is None:
            self.logger.warning("MAIN_SERVICE_NAME environment variable is not set")
        if main_service_host is not None and main_service_name is not None and main_service_host in url:
            url = url.replace(main_service_host, main_service_name)

        image_content = bytes()
        with urllib.request.urlopen(url) as response:
            image_content = response.read()
        self.storage_service.store(image_name, image_content, FileType.IMAGE)
        return self.storage_service.get_image_dir_path() + image_name

    def get_image(self, url):
        pass

    def get_color_palette(self, km):
        colors = km.cluster_centers_
        palette_array = np.round(colors).astype(int).tolist()
        return [{'r': color[0], 'g': color[1], 'b': color[2]} for color in palette_array]

    def get_accent_color(self, km, img_flat_array):
        labels = km.predict(img_flat_array)
        unique, counts = np.unique(labels, return_counts=True)
        dominant_label = unique[np.argmax(counts)]
        dominant_color = km.cluster_centers_[dominant_label]
        accent_array = np.round(dominant_color).astype(int).tolist()
        return {'r': accent_array[0], 'g': accent_array[1], 'b': accent_array[2]}

    def get_kmeans_img_model(self, img: Image.Image):
        n_img = copy.copy(img)
        img_array = np.array(n_img.convert("RGB").getdata())
        img_array_flat = img_array.reshape(-1, 3)
        k = 5
        km = KMeans(n_clusters=k, n_init=10)
        km.fit(img_array_flat)
        return [km, img_array_flat]

    def is_nsfw(self, img_path: str):
        res = self.fx_nsfw_service.send_msg(img_path)
        return res

    def reformat_img(self, img: Image.Image, filepath: str) -> str:
        img.save(filepath, "PNG")
        self.logger.info("Image {} reformatted: {} to {}".format(filepath, img.format, "PNG"))
        return filepath

    def compress_img(self, img):
        pass

    def create_variant(self, img: Image.Image, new_width: int, filepath: str,
                       name: str) -> kafka_image.ImageVariantData | None:
        n_img = copy.copy(img)
        new_file_path = filepath + "-" + name
        new_height = round(int(n_img.height * (new_width / n_img.width)))
        n_img.thumbnail((new_width, new_height), Image.LANCZOS)
        width, height = n_img.size
        left = round((width - new_width) / 2)
        top = round((height - new_height) / 2)
        right = round((width + new_width) / 2)
        bottom = round((height + new_height) / 2)
        n_img = n_img.crop((left, top, right, bottom))
        try:
            n_img.save(new_file_path, "PNG")
            self.logger.info("Image variant {} created".format(new_file_path))
            with open(new_file_path, "rb") as new_img_var_file:
                new_img_var_data = new_img_var_file.read()
                data: kafka_image.ImageVariantData = {
                    "id": 0,
                    "width": n_img.width,
                    "height": n_img.height,
                    "original": False,
                    "size": os.path.getsize(new_file_path),
                    "title": name,
                    "md5": hashlib.md5(new_img_var_data).hexdigest(),
                    "sha256": hashlib.sha256(new_img_var_data).hexdigest(),
                    "url": new_file_path
                }
                return data
        except OSError:
            self.logger.error("Could not save image {}".format(new_file_path))
        return None

    def create_variants(self, img: Image.Image, filepath: str):
        variants_def = [
            {
                "name": "small",
                "width": 640,
            },
            {
                "name": "medium",
                "width": 1920,
            },
            {
                "name": "large",
                "width": 2400,
            },
            {
                "name": "extra large",
                "width": 3840,
            }
        ]
        variants = []
        for var in variants_def:
            target_width = var["width"]
            if img.size[0] > target_width * 1.2:
                variant_data = self.create_variant(img, target_width, filepath, var["name"])
                if variant_data is not None:
                    variants.append(variant_data)
        return variants
