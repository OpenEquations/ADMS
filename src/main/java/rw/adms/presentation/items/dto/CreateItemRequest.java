package rw.adms.presentation.items.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;

import java.time.LocalDate;

public record CreateItemRequest(

        @NotBlank(message = "Item name is required")
        String itemName,

        @NotBlank(message = "Item description is required")
        String itemDescription,

        @NotNull(message = "Item status is required")
        ItemStatus itemStatus,

        @NotNull(message = "Item type is required")
        ItemType itemType,

        @NotNull(message = "Item health is required")
        @Min(value = 0, message = "Item health must be between 0 and 100")
        @Max(value = 100, message = "Item health must be between 0 and 100")
        Integer itemHealth,

        LocalDate dateBought
) {
}
