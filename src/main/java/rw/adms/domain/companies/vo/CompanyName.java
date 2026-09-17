package rw.adms.domain.companies.vo;

public final class CompanyName {

    private final String value;

    public CompanyName(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Company name cannot be empty"
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

        if (!(o instanceof CompanyName other)) {
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