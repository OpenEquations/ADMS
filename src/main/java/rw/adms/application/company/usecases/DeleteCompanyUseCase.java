package rw.adms.application.company.usecases;

import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.companies.vo.CompanyId;


public class DeleteCompanyUseCase {

    private final CompanyRepository companyRepository;

    public DeleteCompanyUseCase(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void execute(Long companyId) {

        CompanyId id = new CompanyId(companyId);

        if (!companyRepository.existsById(id)) {
            throw new IllegalArgumentException("Company not found");
        }

        companyRepository.deleteById(id);
    }
}