package traulka.test.utilitybill.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set")
public class PayerDto {
    Long id;
    String firstName;
    String lastName;
    LocalDateTime createdTime;
}
