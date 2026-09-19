package rw.adms.presentation.warehouses.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWarehouseRequest(

        @NotBlank(message = "Warehouse name is required")
        String name
) {
}
