package com.example.project.repositories;

import com.example.project.entities.RoleEntity;
import com.example.project.enums.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(RoleEnum name);
}
