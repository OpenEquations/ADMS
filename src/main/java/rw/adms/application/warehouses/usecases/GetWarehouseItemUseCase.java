package rw.adms.application.warehouses.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;

public class GetWarehouseItemUseCase {

    private final WarehouseRepository warehouseRepository;

    public GetWarehouseItemUseCase(
            WarehouseRepository warehouseRepository
    ) {
        this.warehouseRepository = warehouseRepository;
    }

    public Item execute(Long warehouseId, Long itemId) {

        Warehouse warehouse = warehouseRepository.findById(
                new WarehouseId(warehouseId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Warehouse not found"));

        return warehouse.getItem(new ItemId(itemId));
    }
}