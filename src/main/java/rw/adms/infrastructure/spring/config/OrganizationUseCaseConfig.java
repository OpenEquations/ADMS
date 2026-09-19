package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.organization.usecases.GetOrganizationSettingsUseCase;
import rw.adms.application.organization.usecases.UpdateOrganizationSettingsUseCase;
import rw.adms.domain.organization.interfaces.OrganizationSettingsRepository;

@Configuration
public class OrganizationUseCaseConfig {

    @Bean
    public GetOrganizationSettingsUseCase getOrganizationSettingsUseCase(
            OrganizationSettingsRepository organizationSettingsRepository
    ) {
        return new GetOrganizationSettingsUseCase(organizationSettingsRepository);
    }

    @Bean
    public UpdateOrganizationSettingsUseCase updateOrganizationSettingsUseCase(
            OrganizationSettingsRepository organizationSettingsRepository
    ) {
        return new UpdateOrganizationSettingsUseCase(organizationSettingsRepository);
    }
}
