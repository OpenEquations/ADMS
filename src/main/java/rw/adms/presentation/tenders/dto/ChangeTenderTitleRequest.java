package rw.adms.presentation.tenders.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeTenderTitleRequest(

        @NotBlank(message = "Tender title is required")
        String title
) {
}
