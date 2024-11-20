package com.example.project.repositories;
import com.example.project.entities.CustomerProfileEntity;
import com.example.project.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends CrudRepository<CustomerProfileEntity, String> {
    Optional<CustomerProfileEntity> findByUser(UserEntity user);
}

