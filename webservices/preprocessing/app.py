import sys
from confluent_kafka import Consumer, KafkaException, KafkaError
from containers import root_container
from services.kafka import KafkaTopics

logger = root_container.logger_service()


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
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    sys.stderr.write('%% %s [%d] reached end at offset %d\n' %
                                     (msg.topic(), msg.partition(), msg.offset()))
                elif msg.error():
                    raise KafkaException(msg.error())
            else:
                logger.info("Received message: {}".format(msg.value().decode('utf-8')))
                logger.info(msg.topic())
                # message_object = kafka.encode(msg)
    finally:
        consumer.close()


if __name__ == '__main__':
    logger.info("Starting image consumer")
    consume_image_kafka_topic()
