package rw.adms.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Singleton row (fixed id, no auto-generation) holding the disposing
 * organization's own profile.
 */
@Entity
@Table(name = "organization_settings")
public class OrganizationSettingsJpaEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String website;

    private String registrationNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    protected OrganizationSettingsJpaEntity() {
    }

    public OrganizationSettingsJpaEntity(
            Long id,
            String name,
            String email,
            String phone,
            String address,
            String website,
            String registrationNumber,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.website = website;
        this.registrationNumber = registrationNumber;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
