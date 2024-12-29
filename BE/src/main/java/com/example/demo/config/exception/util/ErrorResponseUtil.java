package com.example.demo.config.exception.util;

import com.example.demo.config.exception.dto.ErrorResponseDTO;
import com.example.demo.config.exception.dto.InvalidParameter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

// Không cho phép class khác kế thừa
public final class ErrorResponseUtil {

    //Đảm bảo rằng mỗi method chỉ nên thực hiện 1 công việc cụ thể
    public static ErrorResponseDTO build(final String code, final String message, final HttpStatus status) {
        return buildDetails(code, message, status);
    }

    public static ErrorResponseDTO build(final String code, final String message, final HttpStatus status, final List<InvalidParameter> invalidParameters) {
        return buildDetails(code, message, status, invalidParameters);
    }
    //

    // Mục đích tạo thêm 1 method -> xử lý các case khác nhau với mục đích cụ thể
    // Khi nhìn vào phần code sử dụng sẽ dễ dàng hiểu phần này không truyền invalidParams -> tăng tính dễ đọc code
    private static ErrorResponseDTO buildDetails(final String code, final String message, final HttpStatus status) {
        return buildDetails(code, message, status, null);
    }

    //Thực hiện gán gía trị cho ErrorResponseDTO -> Tái sử dụng -> Tránh việc duplicate code
    private static ErrorResponseDTO buildDetails(final String code, final String message, final HttpStatus status, final List<InvalidParameter> invalidParameters) {
        final ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(); //Không muốn cho thay thế 1 reffrence khác
        errorResponseDTO.setCode(code);
        errorResponseDTO.setMessage(message);
        if (!Objects.isNull(status)) {
            errorResponseDTO.setStatus(status.value());
        }
        errorResponseDTO.setTimestamp(LocalDateTime.now());
        if (!CollectionUtils.isEmpty(invalidParameters)) {
            errorResponseDTO.setInvalidParameters(invalidParameters);
        }
        return errorResponseDTO;
    };
}
