package rw.adms.presentation.auth.dto;

import rw.adms.presentation.users.dto.UserResponse;

public record LoginResponse(
        String token,
        UserResponse user
) {
}
