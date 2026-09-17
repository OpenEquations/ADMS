package rw.adms.application.company.usecases;

import rw.adms.domain.company.Company;
import rw.adms.domain.company.interfaces.CompanyRepository;
import rw.adms.domain.company.vo.CompanyId;

public class ChangeCompanyEmailUseCase {

    private final CompanyRepository companyRepository;

    public ChangeCompanyEmailUseCase(
            CompanyRepository companyRepository
    ) {
        this.companyRepository = companyRepository;
    }

    public void execute(Long companyId, String newEmail) {

        Company company = companyRepository.findById(
                new CompanyId(companyId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Company not found"));

        company.changeCompanyEmail(newEmail);

        companyRepository.save(company);
    }
}