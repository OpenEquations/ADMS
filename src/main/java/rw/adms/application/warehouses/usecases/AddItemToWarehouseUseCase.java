package rw.adms.application.warehouses.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;

public class AddItemToWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;
    private final ItemRepository itemRepository;

    public AddItemToWarehouseUseCase(
            WarehouseRepository warehouseRepository,
            ItemRepository itemRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.itemRepository = itemRepository;
    }

    public void execute(Long warehouseId, Long itemId) {

        Warehouse warehouse = warehouseRepository.findById(
                new WarehouseId(warehouseId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Warehouse not found"));

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        warehouse.addItem(item);

        warehouseRepository.save(warehouse);
    }
}