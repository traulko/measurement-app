package traulka.test.utilitybill.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import traulka.test.utilitybill.entity.type.MeasurementType;
import traulka.test.utilitybill.validation.annotation.ValueOfEnum;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set")
public class CreateMeasurementDto {
    @Min(value = 0, message = "userId value must be > 0")
    Long userId;
    @Digits(integer = 3, fraction = 2)
    @Min(value = 0, message = "value must be > 0")
    Double value;
    @ValueOfEnum(enumClass = MeasurementType.class)
    String type;
}
