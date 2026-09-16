package rw.adms.domain.tenders.vo;

public final class TenderTitle {

    private final String value;

    public TenderTitle(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Tender title cannot be empty"
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
        if (!(o instanceof TenderTitle other)) return false;
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