package com.example.project.repositories;

import com.example.project.entities.CartEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, String> {
    Optional<CartEntity> findByUserIdAndProductId(String userId, String productId);
}
