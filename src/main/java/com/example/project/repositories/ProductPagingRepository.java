package com.example.project.repositories;

import com.example.project.entities.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingRepository extends PagingAndSortingRepository<ProductEntity, String> {
}
