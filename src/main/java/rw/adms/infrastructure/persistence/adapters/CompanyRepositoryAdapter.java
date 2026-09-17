package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.companies.vo.CompanyId;
import rw.adms.infrastructure.persistence.entities.CompanyJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataCompanyRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final SpringDataCompanyRepository repository;

    public CompanyRepositoryAdapter(
            SpringDataCompanyRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(Company company) {

        CompanyJpaEntity entity;

        if (company.getId() == null) {

            entity = new CompanyJpaEntity(
                    company.getName().getValue(),
                    company.getEmail().getValue()
            );

        } else {

            entity = repository.findById(
                    company.getId().getValue()
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "Company not found"
                    )
            );

            entity.setName(
                    company.getName().getValue()
            );

            entity.setEmail(
                    company.getEmail().getValue()
            );
        }

        repository.save(entity);
    }

    @Override
    public Optional<Company> findById(CompanyId id) {

        return repository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<Company> findAll() {

        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(CompanyId id) {

        return repository.existsById(id.getValue());
    }

    @Override
    public void deleteById(CompanyId id) {

        repository.deleteById(id.getValue());
    }

    private Company toDomain(CompanyJpaEntity entity) {

        return Company.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }
}