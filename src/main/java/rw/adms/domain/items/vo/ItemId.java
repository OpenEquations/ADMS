package rw.adms.domain.items.vo;

public final class ItemId {

    private final Long value;

    public ItemId(Long value) {

        if (value == null || value <= 0) {
            throw new IllegalArgumentException(
                    "Item ID must be greater than zero"
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

        if (!(o instanceof ItemId other)) {
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