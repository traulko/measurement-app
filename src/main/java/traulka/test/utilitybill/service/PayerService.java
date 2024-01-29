package traulka.test.utilitybill.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import traulka.test.utilitybill.dto.CreatePayerDto;
import traulka.test.utilitybill.dto.PayerDto;
import traulka.test.utilitybill.entity.Payer;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.mapper.PayerMapper;
import traulka.test.utilitybill.repository.PayerRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PayerService {

    PayerRepository repository;
    PayerMapper mapper;

    public Page<PayerDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    public PayerDto createNewPayer(CreatePayerDto createPayerDto) {
        Payer entityToSave = mapper.toEntity(createPayerDto);
        entityToSave.setCreatedTime(LocalDateTime.now());
        Payer savedPayer = repository.save(entityToSave);
        return mapper.toDto(savedPayer);
    }

    public PayerDto findById(Long id) {
        Payer payer = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format("Payer with id %s not found", id)));
        return mapper.toDto(payer);
    }
}
