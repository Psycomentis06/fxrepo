import unittest
import urllib.error

from PIL import Image

from services import image
from containers import root_container

logger = root_container.logger_service()
storage_service = root_container.storage_service()


class ImageServiceTestCase(unittest.TestCase):
    def test_image_service(self):
        im = image.ImageService(storage_service=storage_service,
                                logger=logger)
        imgs = [
            "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
            "https://wallup.net/wp-content/uploads/2016/03/10/319576-photography-landscape-nature-water-grass-trees-plants-sunrise-lake.jpg",
            "https://www.publicdomainpictures.net/pictures/170000/velka/landschaft-1463581037RbE.jpg",
            "https://bestinau.com.au/wp-content/uploads/2019/01/free-images-download-1.jpg"
        ]
        for index, img in enumerate(imgs):
            image_name = "img-" + str(index)
            try:
                im.save_remote_image(img, image_name)
                local_img = im.get_local_img(image_name)
                self.assertTrue(local_img is not None)
                create_thumb = im.create_thumbnail(image_name)
                self.assertTrue(create_thumb)
            except urllib.error.HTTPError:
                print("Failed to save image: {}".format(img))

    def test_guess_img_type(self):
        im = image.ImageService(storage_service=storage_service,
                                logger=logger)
        img = "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
        img_name = "test_guess_img"
        im.save_remote_image(img, img_name)
        image_filepath = storage_service.get_image_dir_path() + img_name
        pil_img = Image.open(image_filepath)
        self.assertTrue(im.guess_img_type(pil_img) == 'JPEG')
        self.assertFalse(im.is_png(pil_img))
        im.create_thumbnail(pil_img, image_filepath)
        pil_img.close()


if __name__ == '__main__':
    unittest.main()
