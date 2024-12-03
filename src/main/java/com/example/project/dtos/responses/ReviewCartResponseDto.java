package com.example.project.dtos.responses;

import com.example.project.dtos.ProductsInCartDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCartResponseDto {
    private String message;
    private boolean isError;
    private List<ProductsInCartDto> productsInCart;
    private double totalAmount;
    private double totalWithTax;
}
