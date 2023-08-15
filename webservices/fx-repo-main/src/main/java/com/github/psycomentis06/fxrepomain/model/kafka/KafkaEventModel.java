package com.github.psycomentis06.fxrepomain.model.kafka;

import java.time.LocalDateTime;
import java.util.Set;

public record KafkaEventModel<T>(
        String eventId,
        LocalDateTime eventTime,

        Action action,

        Status status,

        Set<Target> targets,

        T payload
) {
}
