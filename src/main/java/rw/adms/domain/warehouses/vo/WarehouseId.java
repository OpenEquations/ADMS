package rw.adms.domain.warehouses.vo;

public final class WarehouseId {

    private final Long value;

    public WarehouseId(Long value) {

        if (value == null || value <= 0) {
            throw new IllegalArgumentException(
                    "Warehouse ID must be greater than zero"
            );
        }

        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof WarehouseId other)) {
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