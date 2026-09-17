package rw.adms.domain.companies.vo;

public final class CompanyEmail {

    private final String value;

    public CompanyEmail(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Company email cannot be empty"
            );
        }

        if (!value.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            throw new IllegalArgumentException(
                    "Invalid company email"
            );
        }

        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CompanyEmail other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}