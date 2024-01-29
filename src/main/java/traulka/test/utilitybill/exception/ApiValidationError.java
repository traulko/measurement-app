package traulka.test.utilitybill.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiValidationError extends ApiExceptionResponse {
    List<String> errors = new ArrayList<>();

    @Builder
    public ApiValidationError(String message, HttpStatus httpStatus, LocalDateTime timestamp) {
        super(message, httpStatus, timestamp);
    }

    public void addValidationError(String error) {
        errors.add(error);
    }
}
