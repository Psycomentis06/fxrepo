package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}
