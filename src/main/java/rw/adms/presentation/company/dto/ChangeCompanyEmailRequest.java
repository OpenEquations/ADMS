package rw.adms.presentation.company.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeCompanyEmailRequest(

        @NotBlank(message = "Company email is required")
        String email
) {
}
