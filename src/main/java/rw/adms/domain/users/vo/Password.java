package rw.adms.domain.users.vo;

public final class Password {

    private final String password;

    public Password(String password) {
        this.password = password;
    }

    public String getValue() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Password other = (Password) o;

        return this.password.equals(other.password);
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}