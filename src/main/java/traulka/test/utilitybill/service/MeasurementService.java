package traulka.test.utilitybill.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import traulka.test.utilitybill.dto.MeasurementDto;
import traulka.test.utilitybill.entity.Measurement;
import traulka.test.utilitybill.exception.DataNotFoundException;
import traulka.test.utilitybill.mapper.MeasurementMapper;
import traulka.test.utilitybill.repository.MeasurementRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MeasurementService {

    MeasurementRepository repository;

    MeasurementMapper mapper;

    public MeasurementDto findById(Long id) {
        Measurement measurement = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format("Measurement with id %s not found", id)));
        return mapper.toDto(measurement);
    }

    public Page<MeasurementDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    public Page<MeasurementDto> findAllByPayerId(Pageable pageable, Long payerId) {
        return repository.findAllByPayerId(pageable, payerId)
                .map(mapper::toDto);
    }
}
