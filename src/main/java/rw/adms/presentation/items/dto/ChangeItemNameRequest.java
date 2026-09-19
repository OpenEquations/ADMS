package rw.adms.presentation.items.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeItemNameRequest(

        @NotBlank(message = "Item name is required")
        String itemName
) {
}
