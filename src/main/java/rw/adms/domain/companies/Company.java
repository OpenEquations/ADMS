package rw.adms.domain.companies;

import rw.adms.domain.companies.vo.CompanyEmail;
import rw.adms.domain.companies.vo.CompanyId;
import rw.adms.domain.companies.vo.CompanyName;

public class Company {

    private CompanyId id;
    private CompanyName name;
    private CompanyEmail email;

    public Company(String name, String email) {
        this.name = new CompanyName(name);
        this.email = new CompanyEmail(email);
    }

    private Company(
            CompanyId id,
            CompanyName name,
            CompanyEmail email
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static Company reconstitute(
            Long id,
            String name,
            String email
    ) {
        return new Company(
                new CompanyId(id),
                new CompanyName(name),
                new CompanyEmail(email)
        );
    }

    public CompanyId getId() {
        return id;
    }

    public CompanyName getName() {
        return name;
    }

    public CompanyEmail getEmail() {
        return email;
    }

    public void changeCompanyName(String name) {
        this.name = new CompanyName(name);
    }

    public void changeCompanyEmail(String email) {
        this.email = new CompanyEmail(email);
    }
}