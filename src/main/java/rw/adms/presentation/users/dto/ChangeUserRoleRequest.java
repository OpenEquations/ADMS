package rw.adms.presentation.users.dto;

import jakarta.validation.constraints.NotNull;
import rw.adms.domain.users.enums.UserRole;

public record ChangeUserRoleRequest(

        @NotNull(message = "Role is required")
        UserRole role
) {
}
