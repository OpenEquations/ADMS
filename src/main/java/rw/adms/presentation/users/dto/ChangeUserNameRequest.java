package rw.adms.presentation.users.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserNameRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName
) {
}
