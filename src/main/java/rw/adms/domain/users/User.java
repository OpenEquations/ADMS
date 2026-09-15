package rw.adms.domain.users;

import rw.adms.domain.users.vo.*;

public class User {

    private Long id;
    private Name name;
    private final Password password;
    private Email email;

    public User(String firstName, String lastName, String email, String password) {
        this.name = new Name(firstName, lastName);
        this.email = new Email(email);
        this.password = new Password(password);
    }

    // getters
    public String getName() {
        return name.getFirstName() + " " + name.getLastName();
    }

    public String getEmail() {
        return email.getEmail();
    }

    // setters
    public boolean changeFirstName(String fn) {
        this.name = this.name.withFirstName(fn);
        return true;
    }

    public boolean changeLastName(String ln) {
        this.name = this.name.withLastName(ln);
        return true;
    }

    public boolean changeName (String fn, String ln){
        this.name = new Name(fn, ln);
        return true;
    }

    public boolean changeEmail(String email) {
        this.email = new Email(email);
        return true;
    }

}