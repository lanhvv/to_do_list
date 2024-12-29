package com.example.demo.config.exception.policy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@Getter
@Setter
@ToString
public class BusinessLogicException extends RuntimeException {
    private HttpStatus httpStatus;
    private String code;
    private String message;

    public BusinessLogicException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BusinessLogicException(String code, String template, HttpStatus httpStatus, Object... args) {
        this.code = code;
        if (args != null && args.length > 0) {
            this.setMessage(MessageFormat.format(template, args));
        }
        this.httpStatus = httpStatus;
    }
}
