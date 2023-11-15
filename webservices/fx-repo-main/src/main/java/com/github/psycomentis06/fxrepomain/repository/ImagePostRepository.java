package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.projection.ImagePostDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagePostRepository extends JpaRepository<ImagePost, String>,
        JpaSpecificationExecutor<ImagePost> {

    Optional<ImagePostDetailsProjection> findBySlugEquals(String slug);
}
