package rw.adms.application.company.usecases;

import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.companies.vo.CompanyId;

public class GetCompanyUseCase {

    private final CompanyRepository companyRepository;

    public GetCompanyUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company execute(Long companyId) {

        return companyRepository.findById(
                new CompanyId(companyId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Company not found"));
    }
}