package rw.adms.presentation.items.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ChangeItemHealthRequest(

        @NotNull(message = "Item health is required")
        @Min(value = 0, message = "Item health must be between 0 and 100")
        @Max(value = 100, message = "Item health must be between 0 and 100")
        Integer itemHealth
) {
}
