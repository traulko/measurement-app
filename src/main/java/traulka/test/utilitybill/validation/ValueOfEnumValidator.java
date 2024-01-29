package traulka.test.utilitybill.validation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import traulka.test.utilitybill.validation.annotation.ValueOfEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PROTECTED)
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value.toString());
    }
}