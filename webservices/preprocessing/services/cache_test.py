import unittest
import cache
from containers import root_container

storage_service = root_container.storage_service()


class CacheServiceTest(unittest.TestCase):
    def test_caching(self):
        c = cache.Cache("image", storage_service)
        c.set("test", "test")
        c.set("test2", "test2")
        c.set("test3", "test3")
        c.set("test4", "test4")
        c.set("test5", "test5")
        c.set("test6", "test6")
        c.set("test7", "test7")
        self.assertTrue(c.get("test3") == "test3")
        self.assertTrue(c.get("t") is None)
        c.delete("test3")
        self.assertFalse(c.get("test3") == "test3")
        self.assertTrue(c.clear())


if __name__ == '__main__':
    unittest.main()
