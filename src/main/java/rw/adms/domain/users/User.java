package rw.adms.domain.users;

import rw.adms.domain.users.enums.Permission;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.vo.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class User {

    private Long id;
    private Name name;
    private Password password;
    private Email email;
    private UserRole role;
    private final Set<Permission> permissions;

    public User(
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        this(firstName, lastName, email, password, UserRole.USER);
    }

    public User(
            String firstName,
            String lastName,
            String email,
            String password,
            UserRole role
    ) {
        this.name = new Name(firstName, lastName);
        this.email = new Email(email);
        this.password = new Password(password);
        this.role = role;
        this.permissions = EnumSet.noneOf(Permission.class);
    }

    private User(
            Long id,
            Name name,
            Email email,
            Password password,
            UserRole role,
            Set<Permission> permissions
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.permissions = permissions.isEmpty()
                ? EnumSet.noneOf(Permission.class)
                : EnumSet.copyOf(permissions);
    }

    public static User reconstitute(
            Long id,
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        return reconstitute(id, firstName, lastName, email, password, UserRole.USER, Set.of());
    }

    public static User reconstitute(
            Long id,
            String firstName,
            String lastName,
            String email,
            String password,
            UserRole role,
            Set<Permission> permissions
    ) {
        return new User(
                id,
                new Name(firstName, lastName),
                new Email(email),
                new Password(password),
                role,
                permissions
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

    public UserRole getRole() {
        return role;
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public boolean hasPermission(Permission permission) {
        return role == UserRole.SUPERADMIN || permissions.contains(permission);
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

    public boolean changeRole(UserRole newRole) {
        if (newRole == null) {
            return false;
        }

        this.role = newRole;
        return true;
    }

    public boolean grantPermission(Permission permission) {
        if (permission == null) {
            return false;
        }

        return permissions.add(permission);
    }

    public boolean revokePermission(Permission permission) {
        if (permission == null) {
            return false;
        }

        return permissions.remove(permission);
    }
}
