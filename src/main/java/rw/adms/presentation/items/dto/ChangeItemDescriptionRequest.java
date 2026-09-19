package rw.adms.presentation.items.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeItemDescriptionRequest(

        @NotBlank(message = "Item description is required")
        String itemDescription
) {
}
