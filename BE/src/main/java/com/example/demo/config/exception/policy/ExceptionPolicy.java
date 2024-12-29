package com.example.demo.config.exception.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExceptionPolicy {
    private String code;
    private String message;
}
