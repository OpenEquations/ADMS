package rw.adms.application.company.usecases;

import rw.adms.domain.company.Company;
import rw.adms.domain.company.interfaces.CompanyRepository;
import rw.adms.domain.company.vo.CompanyId;

public class ChangeCompanyNameUseCase {

    private final CompanyRepository companyRepository;

    public ChangeCompanyNameUseCase(
            CompanyRepository companyRepository
    ) {
        this.companyRepository = companyRepository;
    }

    public void execute(Long companyId, String newName) {

        Company company = companyRepository.findById(
                new CompanyId(companyId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Company not found"));

        company.changeCompanyName(newName);

        companyRepository.save(company);
    }
}