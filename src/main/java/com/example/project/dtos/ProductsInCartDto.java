package com.example.project.dtos;

import com.example.project.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsInCartDto {
    private ProductEntity product;
    private Integer quantityInCart;
}
