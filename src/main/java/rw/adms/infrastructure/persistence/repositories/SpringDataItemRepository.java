package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.ItemJpaEntity;

public interface SpringDataItemRepository
        extends JpaRepository<ItemJpaEntity, Long> {
}