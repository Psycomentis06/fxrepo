package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
