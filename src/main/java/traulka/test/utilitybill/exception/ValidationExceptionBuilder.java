package traulka.test.utilitybill.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;

public class ValidationExceptionBuilder {
    public static ApiValidationError fromBindingErrors(Errors errors) {
        ApiValidationError exception = new ApiValidationError("Validation failed. " + errors.getErrorCount() + " error(s)",
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
        for (ObjectError objectError : errors.getAllErrors()) {
            exception.addValidationError(objectError.getDefaultMessage());
        }
        return exception;
    }
}
