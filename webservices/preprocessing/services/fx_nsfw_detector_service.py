import socket


class FxNsfwDetectorService:
    def __init__(self, container_name="127.0.0.1", port=8000):
        self.port = port
        self.container_name = container_name

    def send_msg(self, msg):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.container_name, self.port))
        s.send(bytes(msg, 'utf-8'))
        msg = s.recv(1024)
        s.close()
        return msg
