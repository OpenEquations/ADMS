package rw.adms.domain.users.vo;

public final class Name {
    private final String firstName;
    private final String lastName;

    public Name(String fn, String ln) {
        // name should not contain a number. validate it here.
        if (fn == null || fn.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
        if (ln == null || ln.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }

        if (fn.matches(".*\\d.*") || ln.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Names cannot contain numbers.");
        }

        this.firstName = fn;
        this.lastName = ln;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // Creates a NEW Name instance, carrying over the existing lastName
    public Name withFirstName(String newFirstName) {
        return new Name(newFirstName, this.lastName);
    }

    public Name withLastName(String newLastName) {
        return new Name(this.firstName, newLastName);
    }

    @Override
    public boolean equals(Object o) {
        // 1. Optimization: If they point to the exact same spot in memory, they are
        // equal
        if (this == o)
            return true;

        // 2. Null and Type Check: If the other object is null or a different class,
        // they aren't equal
        if (o == null || getClass() != o.getClass())
            return false;

        // 3. Cast the object so we can read its fields
        Name other = (Name) o;

        // 4. Structural Comparison: Compare the actual string values
        return this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName);
    }

    @Override
    public int hashCode() {
        // Generates a unique integer hash based entirely on the internal string values
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

}