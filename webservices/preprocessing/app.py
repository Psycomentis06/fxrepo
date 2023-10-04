import hashlib
import json
import os.path
import sys
from confluent_kafka import KafkaException
from containers import root_container
from services.kafka import KafkaTopics, decode
from services.types.kafka_image import ImagePostData
from services.types.kafka_common import KafkaData, Targets, Actions
from PIL import Image as PilImage

image_service = root_container.image_service()
storage_service = root_container.storage_service()
logger = root_container.logger_service()
mongo_client = root_container.mongo_client()
SERVICE_COLLECTION_NAME = "fx_preprocessing_service_collection"
db = mongo_client[SERVICE_COLLECTION_NAME].get_collection()


def process_image(img_data: ImagePostData):
    image_object = img_data['image']
    image_original_variant = image_object['variants'][0]
    image_name = str(image_original_variant['id'])
    image_service.save_remote_image(image_original_variant['url'], image_name)
    image_file_path = storage_service.get_image_dir_path() + image_name
    pil_image = PilImage.open(image_file_path)
    image_original_variant['width'] = pil_image.width
    image_original_variant['height'] = pil_image.height
    image_original_variant['size'] = os.path.getsize(image_file_path)
    with open(image_file_path, 'rb') as image_file:
        image_data = image_file.read()
        image_original_variant['md5'] = hashlib.md5(image_data).hexdigest()
        image_original_variant['sha256'] = hashlib.sha256(image_data).hexdigest()

    thumb_state = image_service.create_thumbnail(pil_image, image_name)
    if thumb_state is True:
        # save Image to storage service
        pass
    if not image_service.is_png(pil_image):
        image_service.reformat_img(pil_image, image_file_path)

    img_data['nsfw'] = image_service.is_nsfw(image_file_path)

    km, flat_array = image_service.get_kmeans_img_model(pil_image)
    image_object['accentColor'] = image_service.get_accent_color(km, flat_array)
    image_object['colorPalette'] = image_service.get_color_palette(km)

    variants_data = image_service.create_variants(pil_image, image_file_path)
    image_object['variants'] = image_object['variants'] + variants_data

    return image_object


def image_topic_handler(msg: str):
    try:
        kafka_data = decode(msg, KafkaData[ImagePostData])
        logger.info("Received image data from event: {}".format(kafka_data['eventId']))
        saved_event = db.find_one({'_id': kafka_data['eventId']})
        if saved_event is not None:
            logger.info(f"Event {kafka_data['eventId']} already processed, skipping")
            return
        logger.info(f"Event {kafka_data['eventId']} not processed, processing")
        match kafka_data['targets']:
            case [Targets.PRE_PROCESSING.value]:
                if kafka_data['action'] == Actions.ADD.value:
                    logger.info("Adding image to preprocessing queue")
                    kafka_data['payload'] = process_image(kafka_data['payload'])
                    db_obj = kafka_data
                    db_obj['_id'] = kafka_data['eventId']
                    db.insert_one(db_obj)
                else:
                    logger.info("Skipping event, not targeted for preprocessing")
            case _:
                logger.info("Not targeted for preprocessing")
    except json.JSONDecodeError:
        logger.error("Failed to decode image data: {}".format(msg))


def on_assign(consumer, part):
    print("Assigned to: {}".format(list(map(lambda x: x.value(), consumer.assignment()))))


def on_revoke(consumer, part):
    print("Revoked")


def on_lost(consumer, part):
    print("Lost")


def consume_image_kafka_topic():
    consumer = root_container.kafka_consumer_service()
    try:
        topics = [KafkaTopics.IMAGE]
        consumer.subscribe(topics, on_assign=on_assign, on_revoke=on_revoke, on_lost=on_lost)
        logger.info("Listening for topics: {}".format(topics))
        while True:
            msg = consumer.poll(timeout=1.0)
            if msg is None: continue
            if msg.error():
                if msg.error().code() == -191:  # KafkaError._PARTITION_EOF
                    sys.stderr.write('%% %s [%d] reached end at offset %d\n' %
                                     (msg.topic(), msg.partition(), msg.offset()))
                elif msg.error():
                    raise KafkaException(msg.error())
            else:
                logger.info("Received message")
                # message_object = kafka.encode(msg)
                match msg.topic():
                    case KafkaTopics.IMAGE:
                        logger.info("Image topic event")
                        image_topic_handler(msg.value().decode('utf-8'))
                    case _:
                        logger.warn("Unhandled topic: {}".format(msg.topic()))
    finally:
        consumer.close()


if __name__ == '__main__':
    logger.info("Starting image consumer")
    consume_image_kafka_topic()
