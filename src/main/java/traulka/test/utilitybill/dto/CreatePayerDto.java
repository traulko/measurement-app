package traulka.test.utilitybill.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set")
public class CreatePayerDto {

    @Size(min = 2, max = 15, message = "firstName length must be between 2 and 15")
    String firstName;

    @Size(min = 2, max = 15, message = "lastName length must be between 2 and 15")
    String lastName;
}
