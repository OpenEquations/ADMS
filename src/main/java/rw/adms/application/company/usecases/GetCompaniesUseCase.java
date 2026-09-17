package rw.adms.application.company.usecases;

import rw.adms.domain.company.Company;
import rw.adms.domain.company.interfaces.CompanyRepository;

import java.util.List;

public class GetCompaniesUseCase {

    private final CompanyRepository companyRepository;

    public GetCompaniesUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> execute() {
        return companyRepository.findAll();
    }
}