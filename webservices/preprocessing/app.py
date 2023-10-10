import copy
import hashlib
import json
import os.path
import sys
import uuid
from datetime import datetime

from confluent_kafka import KafkaException
from containers import root_container
from services.kafka import KafkaTopics, decode, encode
from services._types.kafka_image import ImagePostData
from services._types.kafka_common import KafkaData, Targets, Actions, Status
from PIL import Image as PilImage

celery_app = root_container.celery_app()
logger = root_container.logger_service()
fx_storage_outer_host = os.getenv("FX_STORAGE_OUTER_HOST")
if fx_storage_outer_host is None:
    logger.error("FX_STORAGE_OUTER_HOST is not set")
    exit(1)
producer = root_container.kafka_producer_service()
image_service = root_container.image_service()
storage_service = root_container.storage_service()
mongo_client = root_container.mongo_client()
fx_storage = root_container.fx_storage()
SERVICE_COLLECTION_NAME = "fx_preprocessing_service_collection"
db = mongo_client.get_default_database().get_collection(SERVICE_COLLECTION_NAME)


@celery_app.task(name="fx_preprocessing_service.process_image", ignore_result=True)
def process_image_task(kafka_data: KafkaData[ImagePostData]):
    kafka_data['payload'] = process_image(kafka_data['payload'])
    db_obj = copy.copy(kafka_data)
    db_obj['_id'] = kafka_data['eventId']
    db_obj['processing_status'] = "SUCCESS"
    db.insert_one(db_obj)
    logger.info("Event {} is processed successfully".format(kafka_data['eventId']))
    kafka_data['eventId'] = str(uuid.uuid4())
    kafka_data['eventTime'] = datetime.now().isoformat()
    kafka_data['action'] = Actions.PROCESSING.value
    kafka_data['targets'] = [Targets.MAIN.value]
    kafka_data['status'] = Status.DONE.value

    def producer_callback(err, msg):
        if err is not None:
            logger.error(f"Error producing message: {err}")
        else:
            logger.info("Message {} produced to topic: {}".format(kafka_data['eventId'], msg.topic()))

    producer.produce(KafkaTopics.IMAGE, encode(kafka_data), callback=producer_callback)
    producer.flush()


def process_image(img_data: ImagePostData):
    image_object = img_data['image']
    image_original_variant = image_object['variants'][0]
    image_name = image_object['id']
    image_original_variant['url'] = image_service.save_remote_image(image_original_variant['url'], image_name)
    image_file_path = storage_service.get_image_dir_path() + image_name
    pil_image = PilImage.open(image_file_path)
    image_original_variant['width'] = pil_image.width
    image_original_variant['height'] = pil_image.height
    image_original_variant['size'] = os.path.getsize(image_file_path)
    with open(image_file_path, 'rb') as image_file:
        image_data = image_file.read()
        image_original_variant['md5'] = hashlib.md5(image_data).hexdigest()
        image_original_variant['sha256'] = hashlib.sha256(image_data).hexdigest()

    thumb_path = image_service.create_thumbnail(pil_image, image_name)
    if thumb_path is not None:
        with open(thumb_path, 'rb') as thumb_file:
            thumb_info = fx_storage.add_image(thumb_file.read())
            if thumb_info is not None:
                img_data['thumbnail'] = fx_storage_outer_host + fx_storage.GET_IMAGE_ENDPOINT.format(
                    thumb_info['info']['Key'])
    if not image_service.is_png(pil_image):
        image_service.reformat_img(pil_image, image_file_path)

    img_data['nsfw'] = image_service.is_nsfw(image_file_path)

    km, flat_array = image_service.get_kmeans_img_model(pil_image)
    image_object['accentColor'] = image_service.get_accent_color(km, flat_array)
    image_object['colorPalette'] = image_service.get_color_palette(km)

    variants_data = image_service.create_variants(pil_image, image_file_path)
    image_object['variants'] = image_object['variants'] + variants_data
    for image in image_object['variants']:
        with open(image['url'], "rb") as file:
            file_info = fx_storage.add_image(file.read())
            if file_info is not None:
                image['url'] = fx_storage_outer_host + fx_storage.GET_IMAGE_ENDPOINT.format(file_info['info']['Key'])

    return img_data


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
                    process_image_task.delay(kafka_data)
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
