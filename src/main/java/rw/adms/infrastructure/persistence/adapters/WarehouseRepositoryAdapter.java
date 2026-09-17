package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;
import rw.adms.domain.warehouses.vo.WarehouseName;
import rw.adms.infrastructure.persistence.entities.ItemJpaEntity;
import rw.adms.infrastructure.persistence.entities.WarehouseJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataItemRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataWarehouseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class WarehouseRepositoryAdapter
        implements WarehouseRepository {

    private final SpringDataWarehouseRepository warehouseRepository;
    private final SpringDataItemRepository itemRepository;

    public WarehouseRepositoryAdapter(
            SpringDataWarehouseRepository warehouseRepository,
            SpringDataItemRepository itemRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void save(Warehouse warehouse) {

        WarehouseJpaEntity entity;

        if (warehouse.getId() == null) {

            entity = new WarehouseJpaEntity(
                    warehouse.getName().getValue()
            );

        } else {

            entity = warehouseRepository.findById(
                    warehouse.getId().getValue()
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "Warehouse not found"
                    )
            );

            entity.setName(
                    warehouse.getName().getValue()
            );
        }

        List<ItemJpaEntity> items =
                warehouse.getItems()
                        .stream()
                        .map(item -> itemRepository.findById(
                                item.getItemId().getValue()
                        ).orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Item not found: "
                                                + item.getItemId()
                                )
                        ))
                        .toList();

        entity.setItems(items);

        warehouseRepository.save(entity);
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId id) {

        return warehouseRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<Warehouse> findByName(
            WarehouseName name
    ) {

        return warehouseRepository
                .findByName(name.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<Warehouse> findAll() {

        return warehouseRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(WarehouseId id) {

        return warehouseRepository.existsById(
                id.getValue()
        );
    }

    @Override
    public boolean existsByName(WarehouseName name) {

        return warehouseRepository.existsByName(
                name.getValue()
        );
    }

    @Override
    public void deleteById(WarehouseId id) {

        warehouseRepository.deleteById(id.getValue());
    }

    private Warehouse toDomain(
            WarehouseJpaEntity entity
    ) {

        List<Item> items =
                entity.getItems()
                        .stream()
                        .map(this::toItem)
                        .toList();

        return Warehouse.reconstitute(
                entity.getId(),
                entity.getName(),
                items
        );
    }

    private Item toItem(ItemJpaEntity entity) {

        return Item.reconstitute(
                entity.getId(),
                entity.getItemName(),
                entity.getItemDescription(),
                entity.getItemStatus(),
                entity.getItemHealth(),
                entity.getDateBought(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                null,
                null,
                null
        );
    }
}