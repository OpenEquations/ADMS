package rw.adms.presentation.tenders.dto;

import jakarta.validation.constraints.NotNull;
import rw.adms.domain.tenders.enums.TenderStatus;

public record ChangeTenderStatusRequest(

        @NotNull(message = "Tender status is required")
        TenderStatus status
) {
}
