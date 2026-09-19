package rw.adms.presentation.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyRequest(

        @NotBlank(message = "Company name is required")
        String name,

        @NotBlank(message = "Company email is required")
        String email
) {
}
