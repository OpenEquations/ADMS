package rw.adms.presentation.users.dto;

import rw.adms.domain.users.User;

/**
 * Never carries the password - only the fields safe to expose over the wire.
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
