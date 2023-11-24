package com.github.psycomentis06.fxrepomain.repository;


import com.github.psycomentis06.fxrepomain.entity.PostViewEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostViewEventRepository extends JpaRepository<PostViewEvent, Integer> {
    Optional<PostViewEvent> findByPostIdAndIpAddrEqualsIgnoreCaseAndUserAgentEqualsIgnoreCaseAndCreatedAtBetween(String post, String ipAddr, String userAgent, LocalDateTime startDate, LocalDateTime endDate);
}
