package rw.adms.presentation.tenders.dto;

import jakarta.validation.constraints.NotNull;

public record AddItemToTenderRequest(

        @NotNull(message = "Item id is required")
        Long itemId
) {
}
