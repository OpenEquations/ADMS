package rw.adms.application.company.usecases;

import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.interfaces.CompanyRepository;

public class CreateCompanyUseCase {

    private final CompanyRepository companyRepository;

    public CreateCompanyUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void execute(
            String companyName,
            String companyEmail
    ) {

        Company company = new Company(
                companyName,
                companyEmail
        );

        companyRepository.save(company);
    }
}