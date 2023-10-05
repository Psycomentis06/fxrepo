import os
import select
import socket
from services.nsfw import NsfwDetector

port = os.getenv("PORT")
if port is None:
    port = 8000
else:
    port = int(port)
storage_image_dir = os.getenv("STORAGE_IMAGE_DIR")
if storage_image_dir is None:
    raise ValueError("Storage image dir not set")

nsfw_service = None
try:
    nsfw_service = NsfwDetector("nsfw.299x299.h5")
except ValueError:
    os.system("./setup.sh")
    nsfw_service = NsfwDetector("nsfw.299x299.h5")


def is_nsfw(img_path: str):
    prediction_res = nsfw_service.predict(img_path)
    sexy = prediction_res[img_path]["sexy"]
    hentai = prediction_res[img_path]["hentai"]
    porn = prediction_res[img_path]["porn"]
    drawings = prediction_res[img_path]["drawings"]
    neutral = prediction_res[img_path]["neutral"]

    total = sexy + hentai + porn + drawings + neutral
    sexy_norm = sexy / total
    hentai_norm = hentai / total
    porn_norm = porn / total

    weight_sexy = 1
    weight_hentai = 1.5
    weight_porn = 2
    weighted_average = (porn_norm * weight_porn) + (sexy_norm * weight_sexy) + (hentai_norm * weight_hentai)
    threshold = 0.8
    print(weighted_average)
    return weighted_average >= threshold


if __name__ == "__main__":
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(("0.0.0.0", port))
    s.listen(5)
    print(f"Listening on port {port}")
    sockets_list = [s]
    while True:
        read_sockets, _, error_sockets = select.select(sockets_list, [], sockets_list)

        for notified_socket in read_sockets:
            if notified_socket == s:
                client_socket, client_address = s.accept()
                sockets_list.append(client_socket)
                print(f"New connection from {client_address}")
            else:
                message = notified_socket.recv(1024)
                if message:
                    print(f"Received message: {message}")
                    notified_socket.send(b'Thank you for connecting')
                else:
                    print("Client closed connection")
                    sockets_list.remove(notified_socket)
                    notified_socket.close()

        for notified_socket in error_sockets:
            print("Err socket remove")
            sockets_list.remove(notified_socket)
            notified_socket.close()
