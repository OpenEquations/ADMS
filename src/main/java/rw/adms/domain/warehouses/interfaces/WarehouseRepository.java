package rw.adms.domain.warehouses.interfaces;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.vo.WarehouseId;
import rw.adms.domain.warehouses.vo.WarehouseName;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {

    void save(Warehouse warehouse);

    Optional<Warehouse> findById(WarehouseId id);

    Optional<Warehouse> findByName(WarehouseName name);

    List<Warehouse> findAll();

    boolean existsById(WarehouseId id);

    boolean existsByName(WarehouseName name);

    void deleteById(WarehouseId id);
}