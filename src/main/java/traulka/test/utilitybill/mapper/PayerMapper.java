package traulka.test.utilitybill.mapper;

import org.mapstruct.Mapper;
import traulka.test.utilitybill.dto.CreatePayerDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.entity.Payer;

@Mapper(componentModel = "spring")
public interface PayerMapper {
    PayerDto toDto(Payer entity);

    Payer toEntity(CreatePayerDto createPayerDto);
}
