package rw.adms.domain.items.vo;

public final class ItemHealth {

    private final int value;

    public ItemHealth(int value) {
        if (value < 0 || value > 10) {
            throw new IllegalArgumentException(
                    "Item health must be between 0 and 10"
            );
        }

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ItemHealth other)) {
            return false;
        }

        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}