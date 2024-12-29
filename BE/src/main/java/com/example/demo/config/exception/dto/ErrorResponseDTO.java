package com.example.demo.config.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO extends CommonResponseDTO {
    private List<InvalidParameter> invalidParameters;
}
