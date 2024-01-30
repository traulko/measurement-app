package traulka.test.utilitybill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traulka.test.utilitybill.entity.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    Page<Measurement> findAllByPayerId(Pageable pageable, Long payerId);
}
