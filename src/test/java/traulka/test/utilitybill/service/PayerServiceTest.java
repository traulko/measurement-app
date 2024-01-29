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
import traulka.test.utilitybill.dto.CreatePayerDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.entity.Payer;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.mapper.PayerMapper;
import traulka.test.utilitybill.repository.PayerRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class PayerServiceTest {

    @InjectMocks
    PayerService service;

    @Mock
    PayerRepository repository;

    @Mock
    PayerMapper mapper;

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    public void findById_successful(Long userId) {
        when(repository.findById(userId)).thenReturn(Optional.of(Payer.builder().setId(userId).build()));
        when(mapper.toDto(any(Payer.class))).thenReturn(PayerDto.builder().setId(userId).build());

        PayerDto payerDto = service.findById(userId);

        verify(repository, times(1)).findById(userId);
        assertEquals(userId, payerDto.getId());
    }

    @Test
    public void findById_payerNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        verify(repository, times(0)).findById(1L);
        assertThrows(DataNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    public void findAll_successful() {
        Pageable pageable = PageRequest.of(1, 5, Sort.by("id"));

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(Payer.builder().setId(1L).build(),
                        Payer.builder().setId(2L).build())));
        when(mapper.toDto(any(Payer.class))).thenReturn(PayerDto.builder().build());

        Page<PayerDto> payerDtoPage = service.findAll(pageable);

        verify(repository, times(1)).findAll(pageable);
        assertEquals(payerDtoPage.getTotalElements(), 2);
        assertEquals(payerDtoPage.getTotalPages(), 1);
    }

    @Test
    public void createNewPayer_successful() {
        CreatePayerDto createPayerDto = CreatePayerDto.builder()
                .setFirstName("Ivan")
                .setLastName("Ivanovich")
                .build();
        Payer afterMappingToEntity = Payer.builder()
                .setFirstName("Ivan")
                .setLastName("Ivanovich")
                .build();

        Clock clock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);

        afterMappingToEntity.setCreatedTime(dateTime);

        Payer savedPayer = Payer.builder()
                .setId(1L)
                .setFirstName("Ivan")
                .setLastName("Ivanovich")
                .setCreatedTime(dateTime)
                .build();

        PayerDto savedPayerAfterMappingToDto = PayerDto.builder()
                .setId(1L)
                .setFirstName("Ivan")
                .setLastName("Ivanovich")
                .setCreatedTime(dateTime)
                .build();

        when(mapper.toEntity(createPayerDto)).thenReturn(afterMappingToEntity);
        when(repository.save(afterMappingToEntity)).thenReturn(savedPayer);
        when(mapper.toDto(any(Payer.class))).thenReturn(savedPayerAfterMappingToDto);

        PayerDto createdPayer = service.createNewPayer(createPayerDto);

        verify(repository, times(1)).save(afterMappingToEntity);
        assertEquals(createdPayer.getId(), 1L);
        assertEquals(createdPayer.getCreatedTime(), dateTime);
    }

}