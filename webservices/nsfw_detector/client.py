import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("127.0.0.1", 8000))
s.send(b"Hello, world")
msg = s.recv(1024)
print(msg)
s.close()
