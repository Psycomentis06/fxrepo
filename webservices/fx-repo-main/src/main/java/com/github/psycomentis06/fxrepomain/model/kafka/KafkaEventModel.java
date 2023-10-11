package com.github.psycomentis06.fxrepomain.model.kafka;

import java.util.Set;

public record KafkaEventModel<T>(
        String eventId,
        String eventTime,

        Action action,

        Status status,

        Set<Target> targets,

        T payload
) {
}
