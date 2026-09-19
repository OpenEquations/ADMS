package rw.adms.presentation.tenders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import rw.adms.domain.tenders.enums.TenderType;

public record CreateTenderRequest(

        @NotBlank(message = "Tender title is required")
        String title,

        String description,

        @NotNull(message = "Tender type is required")
        TenderType type
) {
}
