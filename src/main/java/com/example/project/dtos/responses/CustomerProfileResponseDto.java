package com.example.project.dtos.responses;

import com.example.project.entities.CustomerProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileResponseDto {
    private String message;
    private boolean isError;
    private CustomerProfileEntity customerProfile;
}
