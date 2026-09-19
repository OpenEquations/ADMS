package rw.adms.presentation.tenders.dto;

import jakarta.validation.constraints.NotNull;

public record SetTenderWinnerRequest(

        @NotNull(message = "Company id is required")
        Long companyId
) {
}
