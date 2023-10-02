import sys
from confluent_kafka import KafkaException
from containers import root_container
from services.kafka import KafkaTopics, decode
from services.types.kafka_image import ImagePostData
from services.types.kafka_common import KafkaData

logger = root_container.logger_service()


def image_topic_handler(msg: str):
    image_data = decode(msg, KafkaData[ImagePostData])
    logger.info("Received image data from event: {}".format(image_data['eventId']))
    logger.info("Received image data: {}".format(image_data['payload']))


def consume_image_kafka_topic():
    consumer = root_container.kafka_consumer_service()
    try:
        topics = [KafkaTopics.IMAGE]
        consumer.subscribe(topics)
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
                logger.info(msg.topic())
                # message_object = kafka.encode(msg)
                match msg.topic():
                    case KafkaTopics.IMAGE:
                        logger.info("Image topic event event")
                        image_topic_handler(msg.value().decode('utf-8'))
                    case _:
                        logger.warn("Unhandled topic: {}".format(msg.topic()))
    finally:
        consumer.close()


if __name__ == '__main__':
    logger.info("Starting image consumer")
    consume_image_kafka_topic()
