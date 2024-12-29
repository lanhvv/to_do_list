package com.example.demo.config.exception.util;

import com.example.demo.config.exception.dto.InvalidParameter;
import com.example.demo.config.exception.dto.SuccessResponseDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SuccessResponseUtil {
    public static SuccessResponseDTO<?> build(final String code, final String message, final HttpStatus status) {
        return buildDetails(code, message, status);
    }

    public static SuccessResponseDTO<?> build(final String code, final String message, final HttpStatus status, final List<InvalidParameter> invalidParameters) {
        return buildDetails(code, message, status, invalidParameters);
    }
    //

    // Mục đích tạo thêm 1 method -> xử lý các case khác nhau với mục đích cụ thể
    // Khi nhìn vào phần code sử dụng sẽ dễ dàng hiểu phần này không truyền invalidParams -> tăng tính dễ đọc code
    private static SuccessResponseDTO<?> buildDetails(final String code, final String message, final HttpStatus status) {
        return buildDetails(code, message, status, null);
    }

    //Thực hiện gán gía trị cho SuccessResponseDTO<?> -> Tái sử dụng -> Tránh việc duplicate code
    private static <T> SuccessResponseDTO<?> buildDetails(final String code, final String message, final HttpStatus status, final T content ) {
        final SuccessResponseDTO<T> successResponseDTO = new SuccessResponseDTO<>(); //Không muốn cho thay thế 1 reffrence khác
        successResponseDTO.setCode(code);
        successResponseDTO.setMessage(message);
        if (!Objects.isNull(status)) {
            successResponseDTO.setStatus(status.value());
        }
        successResponseDTO.setTimestamp(LocalDateTime.now());
        successResponseDTO.setContent(content);
        return successResponseDTO;
    };
}
