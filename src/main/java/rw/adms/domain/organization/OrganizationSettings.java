package rw.adms.domain.organization;

/**
 * The disposing organization's own profile - name, contact details, etc.
 * There is exactly one of these per deployment (a singleton row), used to
 * brand tender documents such as the printable poster.
 */
public class OrganizationSettings {

    public static final Long SINGLETON_ID = 1L;

    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String website;
    private final String registrationNumber;
    private final String description;

    public OrganizationSettings(
            String name,
            String email,
            String phone,
            String address,
            String website,
            String registrationNumber,
            String description
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Organization name is required");
        }

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.website = website;
        this.registrationNumber = registrationNumber;
        this.description = description;
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
}
