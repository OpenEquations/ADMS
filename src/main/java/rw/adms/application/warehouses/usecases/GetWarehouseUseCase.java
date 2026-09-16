package rw.adms.application.warehouses.usecases;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;

public class GetWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;

    public GetWarehouseUseCase(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse execute(Long warehouseId) {

        return warehouseRepository.findById(new WarehouseId(warehouseId))
                .orElseThrow(() ->
                        new IllegalArgumentException("Warehouse not found"));
    }
}