package com.example.demo.config.exception;

import com.example.demo.config.exception.dto.ErrorResponseDTO;
import com.example.demo.config.exception.dto.InvalidParameter;
import com.example.demo.config.exception.policy.BusinessLogicException;
import com.example.demo.config.exception.util.ErrorResponseUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.singletonList;


@ControllerAdvice // bắt exception từ controller
@Order(Ordered.HIGHEST_PRECEDENCE) // xác định thứ tự ưu tiên khởi tạo bean
@Slf4j  // log
public class GlobalHandlerException extends ResponseEntityExceptionHandler {
    //ResponseEntityExceptionHandler -> spring  xử lý error từ controller
    // todo: các param method được inject vào không hẳn là chính reference đó. Nó có thể được upper cast -> Cần lưu ý tránh hiểu lầm

    // Sử dụng Exception.class để bắt toàn bộ exception -> Vì xảy ra exception thì coi là lỗi status = 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handUncaughtException(final Exception ex, final ServletWebRequest servletWebRequest) {
        //Exception -> exception muốn bắt -> Khi xảy ra lỗi exception được throw sẽ được inject vào param ex
        //ServletWebRequest -> có extend từ WebRequest -> mang lại nhiều thông tin hơn từ Request
        log(ex, servletWebRequest);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link BusinessLogicException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<Object> handleCustomUncaughtBusinessException(final BusinessLogicException ex,
                                                                        final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDTO errorResponseDto = ErrorResponseUtil.build(ex.getCode(), ex.getMessage(), ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponseDto);
    }

//    /**
//     * Handles the uncaught {@link ApplicationException} exceptions and returns a JSON formatted response.
//     *
//     * @param ex      the ex
//     * @param request the request on which the ex occurred
//     * @return a JSON formatted response containing the ex details and additional fields
//     */
//    @ExceptionHandler({ApplicationException.class})
//    public ResponseEntity<Object> handleCustomUncaughtApplicationException(final ApplicationException ex,
//                                                                           final ServletWebRequest request) {
//        log(ex, request);
//        final ErrorResponseDto errorResponseDto = build(ex.getCode(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
//    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log(ex, (ServletWebRequest) request);
//        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(), ex.getMessage(), (HttpStatus) status);
//        return ResponseEntity.status(status.value()).body(errorResponseDTO);
        // cast thì được thôi nhưng người đọc code sẽ khó hiểu khi không biết status là gì -> đâu ai có thể nhớ hết mã lỗi
        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }



    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log(ex, (ServletWebRequest) request);
        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(), ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDTO);
    }

    //validate lv method object requestBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log(ex,(ServletWebRequest) request);
        final List<InvalidParameter> invalidParameters = ex.getFieldErrors().stream()
                .map(s -> InvalidParameter.builder().param(s.getField()).message(s.getDefaultMessage()).build())
                .collect(Collectors.toList());
        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, invalidParameters);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    //validate param lv class pathVariable or requestParam
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, ServletWebRequest request) {
        log(ex, request);
        final List<InvalidParameter> invalidParameters = ex.getConstraintViolations().stream()
                .map(s -> InvalidParameter.builder().param(s.getInvalidValue().toString()).message(s.getMessage()).build()).collect(Collectors.toList());
        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, invalidParameters);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log(ex, (ServletWebRequest) request);

        final String missingParameter;
        final String missingParameterType;

        if (ex instanceof MissingRequestHeaderException) {
            missingParameter = ((MissingRequestHeaderException) ex).getHeaderName();
            missingParameterType = "header";
        } else if (ex instanceof MissingServletRequestParameterException) {
            missingParameter = ((MissingServletRequestParameterException) ex).getParameterName();
            missingParameterType = "query";
        } else if (ex instanceof MissingPathVariableException) {
            missingParameter = ((MissingPathVariableException) ex).getVariableName();
            missingParameterType = "path";
        } else {
            missingParameter = "unknown";
            missingParameterType = "unknown";
        }

        final InvalidParameter missingParameterDto = InvalidParameter.builder()
                .param(missingParameter)
                .message(format("Missing %s parameter with name '%s'", missingParameterType, missingParameter))
                .build();

        final ErrorResponseDTO errorResponseDto = ErrorResponseUtil.build(ex.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, singletonList(missingParameterDto));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleServletRequestBindingException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleServletRequestBindingException(ex, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log(ex, (ServletWebRequest) request);
        String parameter = ex.getPropertyName();
        if (ex instanceof MethodArgumentTypeMismatchException) {
            parameter = ((MethodArgumentTypeMismatchException) ex).getName();
        }
        final ErrorResponseDTO errorResponseDTO = ErrorResponseUtil.build(ex.getClass().getSimpleName(),
                String.format("Unexpected type specified for '%s' parameter. Required '%s'", parameter, ex.getRequiredType()),
                HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    private void log(final Exception ex, final ServletWebRequest servletWebRequest) {
        final Optional<HttpMethod> httpMethod; // method của request
        final Optional<String> requestUrl;

        final Optional<ServletWebRequest> possibleInComingNullRequest = Optional.ofNullable(servletWebRequest); // không biết
        if (possibleInComingNullRequest.isPresent()) {
            httpMethod = Optional.ofNullable(possibleInComingNullRequest.get().getHttpMethod()); // Nếu param = null -> reutrn Optional.of(null)
            if (Optional.ofNullable(possibleInComingNullRequest.get().getRequest()).isPresent()) {
                requestUrl = Optional.of(possibleInComingNullRequest.get().getRequest().getRequestURL().toString());
            } else {
                requestUrl = Optional.empty();
            }
        } else {
            httpMethod = Optional.empty();
            requestUrl = Optional.empty();
        }

        log.error("Request {} {} failed with exception reason: {} "
                , (httpMethod.isPresent() ? httpMethod.get() : "'nul'")
                , (requestUrl.orElse("'null'"))
                , ex.getMessage(), ex);
    }

}
