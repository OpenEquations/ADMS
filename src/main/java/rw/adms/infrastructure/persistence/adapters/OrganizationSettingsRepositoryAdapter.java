package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.organization.OrganizationSettings;
import rw.adms.domain.organization.interfaces.OrganizationSettingsRepository;
import rw.adms.infrastructure.persistence.entities.OrganizationSettingsJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataOrganizationSettingsRepository;

import java.util.Optional;

@Repository
public class OrganizationSettingsRepositoryAdapter implements OrganizationSettingsRepository {

    private final SpringDataOrganizationSettingsRepository repository;

    public OrganizationSettingsRepositoryAdapter(
            SpringDataOrganizationSettingsRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public OrganizationSettings save(OrganizationSettings settings) {

        OrganizationSettingsJpaEntity entity = repository
                .findById(OrganizationSettings.SINGLETON_ID)
                .orElse(new OrganizationSettingsJpaEntity(
                        OrganizationSettings.SINGLETON_ID,
                        settings.getName(),
                        settings.getEmail(),
                        settings.getPhone(),
                        settings.getAddress(),
                        settings.getWebsite(),
                        settings.getRegistrationNumber(),
                        settings.getDescription()
                ));

        entity.setName(settings.getName());
        entity.setEmail(settings.getEmail());
        entity.setPhone(settings.getPhone());
        entity.setAddress(settings.getAddress());
        entity.setWebsite(settings.getWebsite());
        entity.setRegistrationNumber(settings.getRegistrationNumber());
        entity.setDescription(settings.getDescription());

        OrganizationSettingsJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<OrganizationSettings> find() {

        return repository.findById(OrganizationSettings.SINGLETON_ID)
                .map(this::toDomain);
    }

    private OrganizationSettings toDomain(OrganizationSettingsJpaEntity entity) {

        return new OrganizationSettings(
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getWebsite(),
                entity.getRegistrationNumber(),
                entity.getDescription()
        );
    }
}
