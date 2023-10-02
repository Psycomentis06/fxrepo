import unittest
from fx_storage import FxStorage
import sys

sys.path.append("../")
from containers import root_container

fx_s = root_container.fx_storage()
im_s = root_container.image_service()
storage_service = root_container.storage_service()


class FxStorageTest(unittest.TestCase):
    def test_fx_storage(self):
        img = "https://images.unsplash.com/photo-1682685797828-d3b2561deef4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80"
        img_name = "test_fx_storage"
        im_s.save_remote_image(img, img_name)
        image_filepath = storage_service.get_image_dir_path() + img_name
        with open(image_filepath, "rb") as f:
            img_d = fx_s.add_image(f.read())
            self.assertIsNotNone(img_d)
            img_id = img_d['info']['Key']
            img = fx_s.get_image(img_id)
            self.assertIsNotNone(img)
            print(img[:20])
            rm_res = fx_s.remove_image(img_id)
            self.assertEqual(rm_res['stat'], 8)


if __name__ == '__main__':
    unittest.main()
