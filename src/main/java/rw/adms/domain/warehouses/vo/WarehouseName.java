package rw.adms.domain.warehouses.vo;

public final class WarehouseName {

    private final String value;

    public WarehouseName(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Warehouse name cannot be empty"
            );
        }

        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof WarehouseName other)) {
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