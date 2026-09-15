package rw.adms.domain.users.vo;

public final class Email {
    private final String email;

    public Email(String email) {
        // varidate email here
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Email other = (Email) o;

        return this.email.equals(other.email);
    }

    @Override
    public int hashCode() {

        int result = email.hashCode();
        return result;
    }
}
