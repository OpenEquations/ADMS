package rw.adms.presentation.company.dto;

import rw.adms.domain.companies.Company;

public record CompanyResponse(
        Long id,
        String name,
        String email
) {

    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId().getValue(),
                company.getName().getValue(),
                company.getEmail().getValue()
        );
    }
}
