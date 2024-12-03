package com.example.project.dtos.responses;

import com.example.project.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCartResponseDto {
    private ProductEntity product;
    private boolean isError;
    private Integer quantityInCart;
}
