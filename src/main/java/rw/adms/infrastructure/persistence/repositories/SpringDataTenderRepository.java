package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.TenderJpaEntity;

public interface SpringDataTenderRepository
        extends JpaRepository<TenderJpaEntity, Long> {
}