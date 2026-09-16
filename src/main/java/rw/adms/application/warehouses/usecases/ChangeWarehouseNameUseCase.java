package rw.adms.application.warehouses.usecases;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

public class ChangeWarehouseNameUseCase {

    private final WarehouseRepository warehouseRepository;

    public ChangeWarehouseNameUseCase(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public void execute(Long warehouseId, String newName) {

        Warehouse warehouse = warehouseRepository.findById(
                new rw.adms.domain.warehouses.vo.WarehouseId(warehouseId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Warehouse not found"));

        warehouse.changeName(newName);

        warehouseRepository.save(warehouse);
    }
}