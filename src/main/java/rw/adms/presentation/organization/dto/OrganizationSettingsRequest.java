package rw.adms.presentation.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganizationSettingsRequest(

        @NotBlank(message = "Organization name is required")
        String name,

        @Email(message = "Invalid email address")
        String email,

        String phone,

        String address,

        String website,

        String registrationNumber,

        String description
) {
}
