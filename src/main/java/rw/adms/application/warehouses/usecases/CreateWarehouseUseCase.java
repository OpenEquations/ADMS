package rw.adms.application.warehouses.usecases;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

public class CreateWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;

    public CreateWarehouseUseCase(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse execute(String warehouseName) {

        Warehouse warehouse = new Warehouse(warehouseName);

        return warehouseRepository.save(warehouse);
    }
}