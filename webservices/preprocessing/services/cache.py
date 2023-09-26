import storage
import json


class Cache:
    _filename = "cache"

    def __init__(self, scope="global", storage_service: storage.Storage = None):
        self._storage_service = storage_service
        self._filename += ":" + scope
        exists = self._storage_service.file_exists(self._filename, storage.FileType.CACHE)
        if not exists:
            self._storage_service.store(self._filename, str.encode("{}"), storage.FileType.CACHE)
        self.file_path = self._storage_service.get_dir_sub_path(storage.FileType.CACHE) + self._filename

    def get(self, key: str) -> None | str:
        with open(self.file_path, 'r') as file:
            data = json.load(file)
            return data.get(key)

    def set(self, key: str, value: str) -> None:
        with open(self.file_path, 'r+') as file:
            data = json.load(file)
            data[key] = value
            file.seek(0)
            json.dump(data, file)
            file.truncate()

    def delete(self, key: str):
        with open(self.file_path, 'r+') as file:
            data = json.load(file)
            data.pop(key, None)
            file.seek(0)
            json.dump(data, file)
            file.truncate()

    def clear(self) -> bool:
        return self._storage_service.delete(self._filename, storage.FileType.CACHE)
