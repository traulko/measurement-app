package traulka.test.utilitybill.mapper;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import traulka.test.utilitybill.dto.CreateMeasurementDto;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.entity.Measurement;
import traulka.test.utilitybill.entity.type.MeasurementType;

@Mapper(componentModel = "spring", uses = {PayerMapper.class})
public interface MeasurementMapper {
    MeasurementDto toDto(Measurement measurement);

    Measurement toEntity(CreateMeasurementDto createMeasurementDto);

    @BeforeMapping
    default void mapType(CreateMeasurementDto createMeasurementDto, @MappingTarget Measurement measurement) {
        measurement.setType(MeasurementType.valueOf(createMeasurementDto.getType()));
    }
}
