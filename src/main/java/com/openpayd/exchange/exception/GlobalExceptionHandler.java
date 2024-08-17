package com.openpayd.exchange.exception;

import com.openpayd.exchange.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author suleyman.yildirim
 */
@RestControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    // Handles validation errors and returns a 400 Bad Request
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        logException(ex);
        String errorMessage = ErrorCode.VALIDATION_ERROR.formatMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handles type mismatch errors and returns a 400 Bad Request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MissingServletRequestParameterException ex) {
        logException(ex);
        String errorMessage = ErrorCode.VALIDATION_ERROR.formatMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_ERROR.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex) {
        logException(ex);
        String errorMessage = ErrorCode.VALIDATION_ERROR.formatMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Fallback handler for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logException(ex);
        String errorMessage = ErrorCode.INTERNAL_SERVER_ERROR.formatMessage(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Utility method to log exceptions with stack trace
    private void logException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
    }

}

