package rw.adms.presentation.items.dto;

import jakarta.validation.constraints.NotNull;
import rw.adms.domain.items.enums.ItemStatus;

public record ChangeItemStatusRequest(

        @NotNull(message = "Item status is required")
        ItemStatus itemStatus
) {
}
