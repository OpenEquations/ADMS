package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.items.ItemHealthRecord;
import rw.adms.domain.items.interfaces.ItemHealthRecordRepository;
import rw.adms.infrastructure.persistence.entities.ItemHealthRecordJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataItemHealthRecordRepository;

import java.util.List;

@Repository
public class ItemHealthRecordRepositoryAdapter implements ItemHealthRecordRepository {

    private final SpringDataItemHealthRecordRepository repository;

    public ItemHealthRecordRepositoryAdapter(SpringDataItemHealthRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public ItemHealthRecord save(ItemHealthRecord record) {

        ItemHealthRecordJpaEntity entity = new ItemHealthRecordJpaEntity(
                record.getItemId(),
                record.getHealth(),
                record.getRecordedAt()
        );

        ItemHealthRecordJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public List<ItemHealthRecord> findByItemId(Long itemId) {

        return repository.findByItemIdOrderByRecordedAtAsc(itemId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private ItemHealthRecord toDomain(ItemHealthRecordJpaEntity entity) {

        return new ItemHealthRecord(
                entity.getId(),
                entity.getItemId(),
                entity.getHealth(),
                entity.getRecordedAt()
        );
    }
}
