package rw.adms.presentation.organization.dto;

import rw.adms.domain.organization.OrganizationSettings;

public record OrganizationSettingsResponse(
        String name,
        String email,
        String phone,
        String address,
        String website,
        String registrationNumber,
        String description
) {

    public static OrganizationSettingsResponse from(OrganizationSettings settings) {
        return new OrganizationSettingsResponse(
                settings.getName(),
                settings.getEmail(),
                settings.getPhone(),
                settings.getAddress(),
                settings.getWebsite(),
                settings.getRegistrationNumber(),
                settings.getDescription()
        );
    }

    public static OrganizationSettingsResponse empty() {
        return new OrganizationSettingsResponse(null, null, null, null, null, null, null);
    }
}
