package rw.adms.presentation.warehouses.dto;

import rw.adms.domain.warehouses.Warehouse;
import rw.adms.presentation.items.dto.ItemResponse;

import java.util.List;

public record WarehouseResponse(
        Long id,
        String name,
        List<ItemResponse> items
) {

    public static WarehouseResponse from(Warehouse warehouse) {
        return new WarehouseResponse(
                warehouse.getId().getValue(),
                warehouse.getName().getValue(),
                warehouse.getItems().stream().map(ItemResponse::from).toList()
        );
    }
}
