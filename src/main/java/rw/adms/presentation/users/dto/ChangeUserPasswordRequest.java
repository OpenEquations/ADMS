package rw.adms.presentation.users.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserPasswordRequest(

        @NotBlank(message = "Password is required")
        String password
) {
}
