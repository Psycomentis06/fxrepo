import unittest
import storage
import logging

logger = logging.getLogger(__name__)


class StorageTest(unittest.TestCase):
    def test_init(self):
        st = storage.Storage("/home/psycomentis06/Desktop/fx-repo/abc", logger)
        self.assertIsNotNone(st)

    def test_load(self):
        st = storage.Storage("/home/psycomentis06/Desktop/fx-repo/", logger)
        file_bytes = bytes([12, 34, 56, 78, 90, 12, 34, 56, 78])
        st.store("file", file_bytes, storage.FileType.IMAGE)
        file = st.load("file", storage.FileType.IMAGE)
        self.assertIsNotNone(file)
        self.assertEqual(file_bytes, file)
        rm_res = st.delete("file", storage.FileType.IMAGE)
        self.assertTrue(rm_res)
