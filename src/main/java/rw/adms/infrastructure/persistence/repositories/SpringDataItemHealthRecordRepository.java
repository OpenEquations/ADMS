package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.ItemHealthRecordJpaEntity;

import java.util.List;

public interface SpringDataItemHealthRecordRepository
        extends JpaRepository<ItemHealthRecordJpaEntity, Long> {

    List<ItemHealthRecordJpaEntity> findByItemIdOrderByRecordedAtAsc(Long itemId);
}
