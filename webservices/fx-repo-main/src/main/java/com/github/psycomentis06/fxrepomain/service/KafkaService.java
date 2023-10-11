package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;

public interface KafkaService {
    final static String IMAGE_TOPIC = "fx_repo_topics_image";
    final static String VIDEO_TOPIC = "fx_repo_topics_video";
    final static String AUDIO_TOPIC = "fx_repo_topics_audio";
    final static String VECTOR_TOPIC = "fx_repo_topics_vector";

    void publishNewImagePostEvent(ImagePost p);

    void consumePreprocessingImageFileEvent();
}
