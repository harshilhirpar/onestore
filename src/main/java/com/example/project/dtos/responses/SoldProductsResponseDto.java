package com.example.project.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldProductsResponseDto {
    private String message;
    private boolean isError;
    private List<SoldProductDto> soldProducts;
}
