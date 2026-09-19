package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.OrganizationSettingsJpaEntity;

public interface SpringDataOrganizationSettingsRepository
        extends JpaRepository<OrganizationSettingsJpaEntity, Long> {
}
