package traulka.test.utilitybill.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import traulka.test.utilitybill.dto.CreateMeasurementDto;
import traulka.test.utilitybill.dto.CreatePayerDto;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.entity.Measurement;
import traulka.test.utilitybill.entity.Payer;
import traulka.test.utilitybill.entity.type.MeasurementType;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.mapper.MeasurementMapper;
import traulka.test.utilitybill.repository.MeasurementRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class MeasurementServiceTest {

    @InjectMocks
    MeasurementService service;

    @Mock
    MeasurementRepository repository;

    @Mock
    MeasurementMapper mapper;

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    public void findById_successful(Long measurementId) {
        when(repository.findById(measurementId)).thenReturn(Optional.of(Measurement.builder().setId(measurementId).build()));
        when(mapper.toDto(any(Measurement.class))).thenReturn(MeasurementDto.builder().setId(measurementId).build());

        MeasurementDto payerDto = service.findById(measurementId);

        verify(repository, times(1)).findById(measurementId);
        assertEquals(measurementId, payerDto.getId());
    }

    @Test
    public void findById_measurementNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        verify(repository, times(0)).findById(1L);
        assertThrows(DataNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    public void findAll_successful() {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(Measurement.builder().setId(1L).build(),
                        Measurement.builder().setId(2L).build())));
        when(mapper.toDto(any(Measurement.class))).thenReturn(MeasurementDto.builder().build());

        Page<MeasurementDto> measurementDtoPage = service.findAll(pageable);

        verify(repository, times(1)).findAll(pageable);
        assertEquals(measurementDtoPage.getTotalElements(), 2);
        assertEquals(measurementDtoPage.getTotalPages(), 1);
    }

    @Test
    public void findAllByPayerId_successful() {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));

        MeasurementDto measurementDtoAfterMapping = MeasurementDto.builder()
                .setPayer(PayerDto.builder()
                        .setId(1L)
                        .build())
                        .build();

        when(repository.findAllByPayerId(any(Pageable.class), any(Long.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(Measurement.builder().setId(1L).build(),
                        Measurement.builder().setId(2L).build())));
        when(mapper.toDto(any(Measurement.class))).thenReturn(measurementDtoAfterMapping);

        Page<MeasurementDto> measurementDtoPage = service.findAllByPayerId(pageable, 1L);

        verify(repository, times(1)).findAllByPayerId(pageable, 1L);
        assertEquals(measurementDtoPage.getTotalElements(), 2);
        assertEquals(measurementDtoPage.getTotalPages(), 1);
        assertEquals(measurementDtoPage.getContent().get(0).getPayer().getId(), 1L);
        assertEquals(measurementDtoPage.getContent().get(1).getPayer().getId(), 1L);
    }

    @Test
    public void findAllByPayerId_payerNotFound() {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));
        when(repository.findAllByPayerId(pageable, 1L)).thenReturn(new PageImpl<>(new ArrayList<>()));

        Page<MeasurementDto> allByPayerId = service.findAllByPayerId(pageable, 1L);

        verify(repository, times(1)).findAllByPayerId(pageable, 1L);
        assertEquals(allByPayerId.getTotalElements(), 0);
    }

}