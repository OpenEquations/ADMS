package rw.adms.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.interfaces.CompanyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyRepositoryAdapterTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldSaveAndFindCompany() {

        // Arrange
        Company company = new Company(
                "ACME Rwanda",
                "contact@acme.rw"
        );

        // Act
        Company savedCompany = companyRepository.save(company);

        // Assert
        assertNotNull(savedCompany);
        assertNotNull(savedCompany.getId());

        assertEquals(
                "ACME Rwanda",
                savedCompany.getName().getValue()
        );

        assertEquals(
                "contact@acme.rw",
                savedCompany.getEmail().getValue()
        );
    }

    @Test
    void shouldFindCompanyById() {

        // Arrange
        Company company = new Company(
                "BK Company",
                "info@bk.rw"
        );

        Company savedCompany =
                companyRepository.save(company);

        // Act
        Optional<Company> result =
                companyRepository.findById(
                        savedCompany.getId()
                );

        // Assert
        assertTrue(result.isPresent());

        assertEquals(
                savedCompany.getId(),
                result.get().getId()
        );

        assertEquals(
                "BK Company",
                result.get().getName().getValue()
        );

        assertEquals(
                "info@bk.rw",
                result.get().getEmail().getValue()
        );
    }

    @Test
    void shouldUpdateCompanyName() {

        // Arrange
        Company company = new Company(
                "Old Company",
                "old@example.com"
        );

        Company savedCompany =
                companyRepository.save(company);

        // Act
        savedCompany.changeCompanyName(
                "New Company"
        );

        Company updated =
                companyRepository.save(savedCompany);

        // Assert
        assertEquals(
                savedCompany.getId(),
                updated.getId()
        );

        assertEquals(
                "New Company",
                updated.getName().getValue()
        );
    }

    @Test
    void shouldDeleteCompany() {

        // Arrange
        Company company = new Company(
                "Delete Me",
                "delete@example.com"
        );

        Company savedCompany =
                companyRepository.save(company);

        // Act
        companyRepository.deleteById(
                savedCompany.getId()
        );

        // Assert
        assertFalse(
                companyRepository.existsById(
                        savedCompany.getId()
                )
        );
    }
}