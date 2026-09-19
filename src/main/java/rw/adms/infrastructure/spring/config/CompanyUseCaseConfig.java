package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.company.usecases.ChangeCompanyEmailUseCase;
import rw.adms.application.company.usecases.ChangeCompanyNameUseCase;
import rw.adms.application.company.usecases.CreateCompanyUseCase;
import rw.adms.application.company.usecases.DeleteCompanyUseCase;
import rw.adms.application.company.usecases.GetCompaniesUseCase;
import rw.adms.application.company.usecases.GetCompanyUseCase;
import rw.adms.domain.companies.interfaces.CompanyRepository;

/**
 * Wires the companies bounded context's use cases as Spring beans.
 */
@Configuration
public class CompanyUseCaseConfig {

    @Bean
    public CreateCompanyUseCase createCompanyUseCase(CompanyRepository companyRepository) {
        return new CreateCompanyUseCase(companyRepository);
    }

    @Bean
    public GetCompanyUseCase getCompanyUseCase(CompanyRepository companyRepository) {
        return new GetCompanyUseCase(companyRepository);
    }

    @Bean
    public GetCompaniesUseCase getCompaniesUseCase(CompanyRepository companyRepository) {
        return new GetCompaniesUseCase(companyRepository);
    }

    @Bean
    public ChangeCompanyNameUseCase changeCompanyNameUseCase(CompanyRepository companyRepository) {
        return new ChangeCompanyNameUseCase(companyRepository);
    }

    @Bean
    public ChangeCompanyEmailUseCase changeCompanyEmailUseCase(CompanyRepository companyRepository) {
        return new ChangeCompanyEmailUseCase(companyRepository);
    }

    @Bean
    public DeleteCompanyUseCase deleteCompanyUseCase(CompanyRepository companyRepository) {
        return new DeleteCompanyUseCase(companyRepository);
    }
}
