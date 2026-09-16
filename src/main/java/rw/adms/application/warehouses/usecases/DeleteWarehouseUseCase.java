package rw.adms.application.warehouses.usecases;

import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.domain.warehouses.vo.WarehouseId;

public class DeleteWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;

    public DeleteWarehouseUseCase(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public void execute(Long warehouseId) {

        WarehouseId id = new WarehouseId(warehouseId);

        if (!warehouseRepository.existsById(id)) {
            throw new IllegalArgumentException("Warehouse not found");
        }

        warehouseRepository.deleteById(id);
    }
}