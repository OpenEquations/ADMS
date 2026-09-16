package rw.adms.application.warehouses.usecases;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

import java.util.List;

public class GetWarehousesUseCase {

    private final WarehouseRepository warehouseRepository;

    public GetWarehousesUseCase(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> execute() {
        return warehouseRepository.findAll();
    }
}