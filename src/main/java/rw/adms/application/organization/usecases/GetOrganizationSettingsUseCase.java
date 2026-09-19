package rw.adms.application.organization.usecases;

import rw.adms.domain.organization.OrganizationSettings;
import rw.adms.domain.organization.interfaces.OrganizationSettingsRepository;

import java.util.Optional;

public class GetOrganizationSettingsUseCase {

    private final OrganizationSettingsRepository organizationSettingsRepository;

    public GetOrganizationSettingsUseCase(OrganizationSettingsRepository organizationSettingsRepository) {
        this.organizationSettingsRepository = organizationSettingsRepository;
    }

    public Optional<OrganizationSettings> execute() {
        return organizationSettingsRepository.find();
    }
}
