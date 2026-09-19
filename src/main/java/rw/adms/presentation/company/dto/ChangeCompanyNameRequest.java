package rw.adms.presentation.company.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeCompanyNameRequest(

        @NotBlank(message = "Company name is required")
        String name
) {
}
