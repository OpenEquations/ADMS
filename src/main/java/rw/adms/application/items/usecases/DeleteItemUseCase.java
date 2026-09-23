package rw.adms.application.items.usecases;

import rw.adms.domain.items.interfaces.ItemHealthRecordRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

/**
 * Deleting an item must first detach it from every warehouse and tender
 * still referencing it (both are real foreign-key-backed join tables), or
 * the database rejects the delete outright with a constraint violation.
 * Health history has no such constraint, but is cleaned up too so deleted
 * items don't leave orphaned rows behind.
 */
public class DeleteItemUseCase {

    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final TenderRepository tenderRepository;
    private final ItemHealthRecordRepository itemHealthRecordRepository;

    public DeleteItemUseCase(
            ItemRepository itemRepository,
            WarehouseRepository warehouseRepository,
            TenderRepository tenderRepository,
            ItemHealthRecordRepository itemHealthRecordRepository
    ) {
        this.itemRepository = itemRepository;
        this.warehouseRepository = warehouseRepository;
        this.tenderRepository = tenderRepository;
        this.itemHealthRecordRepository = itemHealthRecordRepository;
    }

    public void execute(Long itemId) {

        ItemId id = new ItemId(itemId);

        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Item not found");
        }

        for (Warehouse warehouse : warehouseRepository.findAll()) {
            if (warehouse.containsItem(id)) {
                warehouse.removeItem(id);
                warehouseRepository.save(warehouse);
            }
        }

        for (Tender tender : tenderRepository.findAll()) {
            if (tender.containsItem(id)) {
                tender.removeItem(id);
                tenderRepository.save(tender);
            }
        }

        itemHealthRecordRepository.deleteByItemId(itemId);

        itemRepository.deleteById(id);
    }
}
