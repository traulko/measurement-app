package traulka.test.utilitybill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traulka.test.utilitybill.entity.Payer;

@Repository
public interface PayerRepository extends JpaRepository<Payer, Long> {
}
