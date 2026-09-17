package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.CompanyJpaEntity;

public interface SpringDataCompanyRepository
        extends JpaRepository<CompanyJpaEntity, Long> {
}