package com.example.project.repositories;

import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessProfileRepository extends CrudRepository<BusinessProfileEntity, String> {
    Optional<BusinessProfileEntity> findByUser(UserEntity user);
}
