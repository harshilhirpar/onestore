package com.example.project.dtos.responses;

import com.example.project.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponseDto {
    String message;
    boolean isError;
    UserEntity data;
}
