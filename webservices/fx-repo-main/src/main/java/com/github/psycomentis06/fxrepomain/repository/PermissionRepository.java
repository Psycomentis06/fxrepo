package com.github.psycomentis06.fxrepomain.repository;

import com.github.psycomentis06.fxrepomain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
