package com.example.demo.config.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponseDTO {
    private String code;
    private String message;
    private Integer status;
    private LocalDateTime timestamp;
}
