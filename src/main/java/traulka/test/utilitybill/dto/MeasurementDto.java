package traulka.test.utilitybill.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import traulka.test.utilitybill.entity.type.MeasurementType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set")
public class MeasurementDto {
    Long id;
    MeasurementType type;
    Double value;
    LocalDateTime createdTime;
    PayerDto payer;
}
