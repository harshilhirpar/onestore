package com.example.project.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadThumbnailResponseDto {
    private String imageUrl;
    private Boolean isError;
    private String message;
}
