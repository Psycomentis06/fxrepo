import unittest
from services.fx_nsfw_detector_service import FxNsfwDetectorService


class FxNsfwDetectorServiceTest(unittest.TestCase):
    def test_connection(self):
        nsfw = FxNsfwDetectorService("172.21.0.2", 8000)
        msg = nsfw.send_msg("Hello there")
        self.assertIsNotNone(msg)
        self.assertTrue(type(msg) == bytes)


if __name__ == '__main__':
    unittest.main()
