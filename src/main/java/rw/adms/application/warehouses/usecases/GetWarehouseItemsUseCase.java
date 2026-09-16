package rw.adms.application.warehouses.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;

import java.util.List;

public class GetWarehouseItemsUseCase {

    private final WarehouseRepository warehouseRepository;

    public GetWarehouseItemsUseCase(
            WarehouseRepository warehouseRepository
    ) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Item> execute(Long warehouseId) {

        Warehouse warehouse = warehouseRepository.findById(
                new WarehouseId(warehouseId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Warehouse not found"));

        return warehouse.getItems();
    }
}