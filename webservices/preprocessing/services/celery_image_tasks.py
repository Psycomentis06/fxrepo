import pathlib

from containers import root_container
from celery import shared_task
import imagehash
import PIL.Image as PilImage
import hashlib

celery_app = root_container.celery_app()
image_service = root_container.image_service()
fx_storage = root_container.fx_storage()
logger = root_container.logger_service()


@shared_task
def get_image_hashes(img_path):
    with open(img_path, 'rb') as image_file:
        image_data = image_file.read()
        md5 = hashlib.md5(image_data).hexdigest()
        sha256 = hashlib.sha256(image_data).hexdigest()
        return {"md5": md5, "sha256": sha256}


@shared_task
def create_thumbnail(img_path, img_name):
    thumb_path = image_service.create_thumbnail(PilImage.open(img_path), img_name)
    if thumb_path is not None:
        with open(thumb_path, 'rb') as thumb_file:
            thumb_info = fx_storage.add_image(thumb_file.read())
            thumbnail_url = ""
            if thumb_info is not None:
                thumbnail_url = fx_storage.outer_endpoint + fx_storage.GET_IMAGE_ENDPOINT.format(
                    thumb_info['info']['Key'])
        try:
            pathlib.Path(thumb_path).unlink()
            logger.info('Deleted thumbnail file: ' + thumb_path)
            return {"thumbnail": thumbnail_url}
        except OSError:
            logger.error('Could not delete thumbnail file: ' + thumb_path)


@shared_task
def create_variants(img_path, existing_variants):
    variants_data = image_service.create_variants(PilImage.open(img_path), img_path)
    variants = existing_variants + variants_data
    for image in variants:
        image_path = image['url']
        with open(image_path, "rb") as file:
            file_info = fx_storage.add_image(file.read())
            if file_info is not None:
                image['url'] = fx_storage.outer_endpoint + fx_storage.GET_IMAGE_ENDPOINT.format(
                    file_info['info']['Key'])
        try:
            if image['original'] is False:
                pathlib.Path(image_path).unlink()
                logger.info('Deleted variant file: ' + image_path)
        except OSError:
            logger.error('Could not delete image file: ' + image_path)
    return {"variants": variants}


@shared_task
def calculate_perceptual_hash(img_path):
    return {"perceptualHash": str(imagehash.phash(PilImage.open(img_path)))}


@shared_task
def calculate_average_hash(img_path):
    return {"averageHash": str(imagehash.average_hash(PilImage.open(img_path)))}


@shared_task
def calculate_difference_hash(img_path):
    return {"differenceHash": str(imagehash.dhash(PilImage.open(img_path)))}


@shared_task
def calculate_color_hash(img_path):
    return {"colorHash": str(imagehash.colorhash(PilImage.open(img_path)))}


@shared_task
def get_image_colors(img_path):
    img = PilImage.open(img_path)
    img.thumbnail((300, 300))
    km, flat_array = image_service.get_kmeans_img_model(img)
    accent_color = image_service.get_accent_color(km, flat_array)
    color_palette = image_service.get_color_palette(km)
    return {"accentColor": accent_color, "colorPalette": color_palette}
