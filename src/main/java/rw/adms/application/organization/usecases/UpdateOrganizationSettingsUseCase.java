package rw.adms.application.organization.usecases;

import rw.adms.domain.organization.OrganizationSettings;
import rw.adms.domain.organization.interfaces.OrganizationSettingsRepository;

public class UpdateOrganizationSettingsUseCase {

    private final OrganizationSettingsRepository organizationSettingsRepository;

    public UpdateOrganizationSettingsUseCase(OrganizationSettingsRepository organizationSettingsRepository) {
        this.organizationSettingsRepository = organizationSettingsRepository;
    }

    public OrganizationSettings execute(
            String name,
            String email,
            String phone,
            String address,
            String website,
            String registrationNumber,
            String description
    ) {
        OrganizationSettings settings = new OrganizationSettings(
                name, email, phone, address, website, registrationNumber, description
        );

        return organizationSettingsRepository.save(settings);
    }
}
