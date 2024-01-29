package traulka.test.utilitybill.service.facade;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import traulka.test.utilitybill.dto.CreateMeasurementDto;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.entity.Measurement;
import traulka.test.utilitybill.entity.Payer;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.mapper.MeasurementMapper;
import traulka.test.utilitybill.repository.MeasurementRepository;
import traulka.test.utilitybill.repository.PayerRepository;

import java.time.LocalDateTime;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CreateNewMeasurementFacade {
    MeasurementRepository measurementRepository;
    PayerRepository payerRepository;
    MeasurementMapper measurementMapper;

    @Transactional
    public MeasurementDto createNewMeasurement(CreateMeasurementDto createMeasurementDto) {
        Payer payer = payerRepository.findById(createMeasurementDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException(String
                        .format("Payer with id %s not found", createMeasurementDto.getUserId())));
        Measurement entityToSave = measurementMapper.toEntity(createMeasurementDto);
        entityToSave.setPayer(payer);
        entityToSave.setCreatedTime(LocalDateTime.now());
        Measurement savedMeasurement = measurementRepository.save(entityToSave);
        return measurementMapper.toDto(savedMeasurement);
    }
}
