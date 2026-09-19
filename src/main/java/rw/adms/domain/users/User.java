package rw.adms.domain.users;

import rw.adms.domain.users.vo.*;

public class User {

    private Long id;
    private Name name;
    private Password password;
    private Email email;

    public User(
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        this.name = new Name(firstName, lastName);
        this.email = new Email(email);
        this.password = new Password(password);
    }

    private User(
            Long id,
            Name name,
            Email email,
            Password password
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User reconstitute(
            Long id,
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        return new User(
                id,
                new Name(firstName, lastName),
                new Email(email),
                new Password(password)
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getFirstName() + " " + name.getLastName();
    }

    public String getFirstName() {
        return name.getFirstName();
    }

    public String getLastName() {
        return name.getLastName();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getValue();
    }

    public boolean changeFirstName(String fn) {
        this.name = this.name.withFirstName(fn);
        return true;
    }

    public boolean changeLastName(String ln) {
        this.name = this.name.withLastName(ln);
        return true;
    }

    public boolean changeName(String fn, String ln) {
        this.name = new Name(fn, ln);
        return true;
    }

    public boolean changeEmail(String email) {
        this.email = new Email(email);
        return true;
    }

    public boolean changePassword(String password) {
        this.password = new Password(password);
        return true;
    }
}