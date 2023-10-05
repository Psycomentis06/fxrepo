import datetime
import unittest

from ._types.kafka_common import KafkaData
from ._types.kafka_image import ImageData
from .kafka import encode, decode


class KafkaTest(unittest.TestCase):
    def test_encode(self):
        st = '{"eventId":"3ddd49f1-7a78-466d-85ec-a499967c2f1d","eventTime":"2023-09-19T13:37:39.693581664",' \
             '"action":"ADD","status":"DONE","targets":["PRE_PROCESSING"],"payload":{' \
             '"id":"6d4cbd4f-c796-49a9-bb1f-f50a67eadea0","slug":"red-car-by-user-20409d","title":"Red Car By user",' \
             '"content":"Red car in the middle of the street","createdAt":"2023-09-19T13:37:39.496706465",' \
             '"updatedAt":"2023-09-19T13:37:39.496706465","postType":"IMAGE","userId":"HelloHowAreYou","publik":true,' \
             '"ready":false,"nsfw":true,"tags":[{"name":"landscape"},{"name":"cars"},{"name":"red"},' \
             '{"name":"streets"}],"category":{"id":1,"name":"Default","description":"This is the default category for ' \
             'uncategorized posts","svgIcon":null,"thumbnail":null,"bgColor":"rgba(0,0,0,1)","fgColor":"rgba(0,0,0,' \
             '1)","color":"rgba(0,0,0,1)","posts":[]},"thumbnail":null,"image":{' \
             '"id":"5a4e05e5-f03d-4657-b58b-80f46c159e77","placement":"MAIN_SERVICE","orphan":false,"variants":[{' \
             '"id":252,"original":true,"width":0,"height":0,"size":0,"title":"Original",' \
             '"url":"http://localhost:9057/api/v1/image/5a4e05e5-f03d-4657-b58b-80f46c159e77","md5":null,' \
             '"sha256":null}],"accentColor":null,"colorPalette":null,"landscape":false,"posts":[]}}}'
        o = decode(st, KafkaData[ImageData])
        self.assertIsNotNone(o)
        self.assertTrue(o['payload'] is not None)
