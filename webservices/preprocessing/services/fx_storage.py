import http.client
import json
import urllib.error
from urllib import request, error
from typing import TypedDict

import requests


class FxStorageFileInfo(TypedDict):
    Bucket: str
    Key: str
    ETag: str
    Size: int
    LastModified: str
    Location: str
    VersionID: str
    Expiration: str
    ExpirationRuleID: str
    ChecksumCRC32: str
    ChecksumCRC32C: str
    ChecksumSHA1: str
    ChecksumSHA256: str


class FxStorageFileData(TypedDict):
    info: FxStorageFileInfo
    stat: bool


class FxRemoveFileResponse(TypedDict):
    stat: int
    message: str


class FxStorage:
    ADD_IMAGE_ENDPOINT = "/api/v1/image"
    GET_IMAGE_ENDPOINT = "/api/v1/image/{}"
    REMOVE_IMAGE_ENDPOINT = "/api/v1/image/{}"

    def __init__(self, host: str, port: int, ssl: bool, username: str, password: str):
        self.username = username
        self.password = password
        self.ssl = ssl
        self.port = port
        self.host = host
        self.endpoint = ("https://" if ssl else "http://") + host + ":" + str(port)
        self._check()

    def _check(self):
        # Raises exception if endpoint is not reachable that stops the app
        request.urlopen(self.endpoint)

    def add_image(self, image_data: bytes) -> FxStorageFileData | None:
        url = self.host + ":" + str(self.port)
        conn = (http.client.HTTPSConnection(url) if self.ssl else http.client.HTTPConnection(url))
        boundary = "------WebKitFormBoundary7MA4YWxkTrZu0gW"
        body = ("--%s\r\n"
                "Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"\r\n"
                "Content-Type: image/png\r\n\r\n"
                "%s\r\n"
                "--%s--\r\n") % (boundary, image_data, boundary)
        headers = {
            "Content-Type": "multipart/form-data; boundary=%s" % boundary,
            "X-FX-STORAGE-USERNAME": self.username,
            "X-FX-STORAGE-PASSWORD": self.password
        }
        conn.request("POST", self.ADD_IMAGE_ENDPOINT, body, headers)
        with conn.getresponse() as res:
            if 300 > res.status >= 200:
                res_data: FxStorageFileData = json.loads(res.read().decode("utf-8"))
                conn.close()
                return res_data
            else:
                conn.close()
                raise urllib.error.HTTPError(url, res.status, res.reason, res.msg, res.fp)

    def remove_image(self, image_id: str) -> FxRemoveFileResponse | None:
        headers = {
            "X-FX-STORAGE-USERNAME": self.username,
            "X-FX-STORAGE-PASSWORD": self.password
        }
        req = request.Request(
            url=self.endpoint + self.REMOVE_IMAGE_ENDPOINT.format(image_id),
            headers=headers,
            method="DELETE"
        )

        with request.urlopen(req) as res:
            data: FxRemoveFileResponse = json.loads(res.read().decode("utf-8"))
            return data

    def get_image(self, image_id: str) -> bytes:
        with request.urlopen(url=self.endpoint + self.GET_IMAGE_ENDPOINT.format(image_id)) as img:
            return img.read()
