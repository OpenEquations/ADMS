package rw.adms.presentation.items.dto;

import jakarta.validation.constraints.NotNull;
import rw.adms.domain.items.enums.ItemType;

public record ChangeItemTypeRequest(

        @NotNull(message = "Item type is required")
        ItemType itemType
) {
}
