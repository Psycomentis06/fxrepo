import socket


class FxNsfwDetectorService:
    def __init__(self, host="127.0.0.1", port=8000):
        self.port = port
        self.host = host

    def send_msg(self, msg):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.host, self.port))
        s.send(bytes(msg, 'utf-8'))
        msg = s.recv(1024)
        msg = str(msg, 'utf-8')
        is_nsfw = False
        if msg.upper() == "TRUE":
            is_nsfw = True
        s.close()
        return is_nsfw
