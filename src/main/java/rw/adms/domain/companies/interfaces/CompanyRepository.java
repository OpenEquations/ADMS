package rw.adms.domain.companies.interfaces;

import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.vo.CompanyId;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    Company save(Company company);

    Optional<Company> findById(CompanyId id);

    List<Company> findAll();

    boolean existsById(CompanyId id);

    void deleteById(CompanyId id);
}