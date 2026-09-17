package rw.adms.domain.company.vo;

public final class CompanyId {

    private final Long value;

    public CompanyId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(
                    "Company ID must be greater than zero"
            );
        }

        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CompanyId other)) {
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
        return value.toString();
    }
}