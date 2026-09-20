package rw.adms.presentation.users.dto;

import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.Permission;
import rw.adms.domain.users.enums.UserRole;

import java.util.Set;

/**
 * Never carries the password - only the fields safe to expose over the wire.
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        Set<Permission> permissions
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getPermissions()
        );
    }
}
