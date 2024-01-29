package traulka.test.utilitybill.service.facade;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import traulka.test.utilitybill.dto.CreateMeasurementDto;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.entity.Measurement;
import traulka.test.utilitybill.entity.Payer;
import traulka.test.utilitybill.entity.type.MeasurementType;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.mapper.MeasurementMapper;
import traulka.test.utilitybill.repository.MeasurementRepository;
import traulka.test.utilitybill.repository.PayerRepository;
import traulka.test.utilitybill.service.MeasurementService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CreateNewMeasurementFacadeTest {

    @InjectMocks
    CreateNewMeasurementFacade facade;

    @Mock
    PayerRepository payerRepository;

    @Mock
    MeasurementRepository measurementRepository;

    @Mock
    MeasurementMapper mapper;

    @Test
    public void createNewMeasurement_successful() {
        CreateMeasurementDto createMeasurementDto = CreateMeasurementDto.builder()
                .setUserId(1L)
                .setValue(10.0)
                .setType("GAS")
                .build();
        Measurement afterMappingToEntity = Measurement.builder()
                .setValue(10.0)
                .setType(MeasurementType.GAS)
                .build();

        when(payerRepository.findById(1L)).thenReturn(Optional.of(Payer.builder().setId(1L).build()));
        when(mapper.toEntity(createMeasurementDto)).thenReturn(afterMappingToEntity);

        Clock clock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);

        afterMappingToEntity.setCreatedTime(dateTime);

        Measurement savedMeasurement = Measurement.builder()
                .setId(1L)
                .setType(MeasurementType.GAS)
                .setValue(10.0)
                .setCreatedTime(dateTime)
                .setPayer(Payer.builder()
                        .setId(1L)
                        .build())
                .build();
        MeasurementDto savedMeasurementAfterMappingToDto = MeasurementDto.builder()
                .setId(1L)
                .setType(MeasurementType.GAS)
                .setValue(10.0)
                .setCreatedTime(dateTime)
                .setPayer(PayerDto.builder()
                        .setId(1L)
                        .build())
                .build();

        when(measurementRepository.save(afterMappingToEntity)).thenReturn(savedMeasurement);
        when(mapper.toDto(any(Measurement.class))).thenReturn(savedMeasurementAfterMappingToDto);

        MeasurementDto createdMeasurementDto = facade.createNewMeasurement(createMeasurementDto);

        verify(measurementRepository, times(1)).save(afterMappingToEntity);
        verify(payerRepository, times(1)).findById(1L);
        assertEquals(createdMeasurementDto.getId(), 1L);
        assertEquals(createdMeasurementDto.getPayer().getId(), 1L);
        assertEquals(createdMeasurementDto.getType(), MeasurementType.GAS);
        assertEquals(createdMeasurementDto.getCreatedTime(), dateTime);

    }

    @Test
    public void createNewMeasurement_payerNotFound() {
        when(payerRepository.findById(1L)).thenReturn(Optional.empty());

        verify(payerRepository, times(0)).findById(1L);
        assertThrows(DataNotFoundException.class, () ->
                facade.createNewMeasurement(CreateMeasurementDto.builder().setUserId(1L).build()));
    }
}