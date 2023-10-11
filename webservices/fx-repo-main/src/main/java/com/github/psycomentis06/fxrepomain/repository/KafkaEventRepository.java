package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.KafkaEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaEventRepository extends JpaRepository<KafkaEvent, String> {
}
