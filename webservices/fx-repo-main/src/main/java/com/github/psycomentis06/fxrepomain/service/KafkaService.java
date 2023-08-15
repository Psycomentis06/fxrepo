package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;

public interface KafkaService {
    final static String IMAGE_TOPIC = "fx_repo_topics_image";

    void publishNewImagePostEvent(ImagePost p);

    void consumePreprocessingImageFileEvent();
}
