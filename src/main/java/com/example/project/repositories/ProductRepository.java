package com.example.project.repositories;

import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, String> {
    List<Optional<ProductEntity>> findAllByBusinessProfile(BusinessProfileEntity businessProfile);
}
