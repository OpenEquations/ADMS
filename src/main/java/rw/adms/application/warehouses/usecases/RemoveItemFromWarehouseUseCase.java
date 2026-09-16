package rw.adms.application.warehouses.usecases;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;
import rw.adms.domain.items.vo.ItemId;

public class RemoveItemFromWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;

    public RemoveItemFromWarehouseUseCase(
            WarehouseRepository warehouseRepository
    ) {
        this.warehouseRepository = warehouseRepository;
    }

    public void execute(Long warehouseId, Long itemId) {

        Warehouse warehouse = warehouseRepository.findById(
                new WarehouseId(warehouseId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Warehouse not found"));

        warehouse.removeItem(new ItemId(itemId));

        warehouseRepository.save(warehouse);
    }
}