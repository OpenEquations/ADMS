package rw.adms.presentation.warehouses.dto;

import jakarta.validation.constraints.NotNull;

public record AddItemToWarehouseRequest(

        @NotNull(message = "Item id is required")
        Long itemId
) {
}
