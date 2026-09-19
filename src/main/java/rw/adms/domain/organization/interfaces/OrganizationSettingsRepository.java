package rw.adms.domain.organization.interfaces;

import rw.adms.domain.organization.OrganizationSettings;

import java.util.Optional;

public interface OrganizationSettingsRepository {

    OrganizationSettings save(OrganizationSettings settings);

    Optional<OrganizationSettings> find();
}
