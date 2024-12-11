package com.example.project.dtos.responses;

import com.example.project.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldProductDto {
    private ProductEntity product;
    private String userId;
    private Integer quantity;
}
