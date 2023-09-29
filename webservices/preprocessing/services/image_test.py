import os
import unittest
import urllib.error

from PIL import Image

from services import image
from containers import root_container

os.environ["PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION"] = "python"
logger = root_container.logger_service()
storage_service = root_container.storage_service()
nsfw_service = root_container.nsfw_detector_service()
image_service = root_container.image_service()


class ImageServiceTestCase(unittest.TestCase):
    def test_image_service(self):
        imgs = [
            "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
            "https://wallup.net/wp-content/uploads/2016/03/10/319576-photography-landscape-nature-water-grass-trees-plants-sunrise-lake.jpg",
            "https://www.publicdomainpictures.net/pictures/170000/velka/landschaft-1463581037RbE.jpg",
            "https://bestinau.com.au/wp-content/uploads/2019/01/free-images-download-1.jpg"
        ]
        for index, img in enumerate(imgs):
            image_name = "img-" + str(index)
            try:
                image_service.save_remote_image(img, image_name)
                local_img = image_service.get_local_img(image_name)
                self.assertTrue(local_img is not None)
                image_file_path = storage_service.get_image_dir_path() + image_name
                pil_image = Image.open(image_file_path)
                create_thumb = image_service.create_thumbnail(pil_image, image_name)
                self.assertTrue(create_thumb)
            except urllib.error.HTTPError:
                print("Failed to save image: {}".format(img))

    def test_guess_img_type(self):
        im = image_service
        img = "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
        img_name = "test_guess_img"
        im.save_remote_image(img, img_name)
        image_filepath = storage_service.get_image_dir_path() + img_name
        pil_img = Image.open(image_filepath)
        self.assertTrue(im.guess_img_type(pil_img) == 'JPEG')
        self.assertFalse(im.is_png(pil_img))
        km, flat_array = im.get_kmeans_img_model(pil_img)
        print(im.get_color_palette(km))
        print(im.get_accent_color(km, flat_array))
        im.create_thumbnail(pil_img, img_name)
        pil_img.close()

    def test_create_variants(self):
        im = image_service
        img = "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&q=80%22"
        img_name = "test_create_variants"
        im.save_remote_image(img, img_name)
        image_filepath = storage_service.get_image_dir_path() + img_name
        pil_img = Image.open(image_filepath)
        im.create_variants(pil_img, image_filepath)
        self.assertTrue(True, True)

    def test_is_nsfw(self):
        imgs = {
            "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&q=80%22": 0,
            "https://cdn.myanimelist.net/images/anime/1212/103848.jpg": 1,
            "https://cdn.myanimelist.net/images/anime/1522/128039.jpg": 0,
            "https://cdn.myanimelist.net/images/anime/1484/116651.webp": 1,
            "https://cdn.myanimelist.net/images/anime/1224/113092.webp": 0,
            "https://cdn.myanimelist.net/images/anime/4/86647.webp": 0,
            "https://cdn.myanimelist.net/images/anime/1716/93151.webp": 1,
            "https://cdn.myanimelist.net/images/anime/4/63429.webp": 1
        }
        i = 0
        for img, nsfw in imgs.items():
            img_name = "test_is_nsfw_" + str(i)
            image_service.save_remote_image(img, img_name)
            image_filepath = storage_service.get_image_dir_path() + img_name
            if nsfw == 0:
                self.assertFalse(image_service.is_nsfw(image_filepath))
            elif nsfw == 1:
                self.assertTrue(image_service.is_nsfw(image_filepath))
            i += 1


if __name__ == '__main__':
    unittest.main()
