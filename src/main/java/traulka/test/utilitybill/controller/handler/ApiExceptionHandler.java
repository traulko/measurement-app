package traulka.test.utilitybill.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import traulka.test.utilitybill.exception.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createApiError(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<ApiExceptionResponse> handleDataNotFoundException(DataNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createApiError(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiValidationError> handleException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(createValidationError(exception));
    }

    private ApiExceptionResponse createApiError(String exceptionMessage, HttpStatus status) {
        return new ApiExceptionResponse(exceptionMessage,
                status,
                LocalDateTime.now());
    }

    private ApiValidationError createValidationError(MethodArgumentNotValidException e) {
        return ValidationExceptionBuilder.fromBindingErrors(e.getBindingResult());
    }

}
