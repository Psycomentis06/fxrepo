class RedisService:
    _redis = "hello"

    def __init__(self, redis="hello"):
        self._redis = redis

    def getRedis(self):
        return self._redis
