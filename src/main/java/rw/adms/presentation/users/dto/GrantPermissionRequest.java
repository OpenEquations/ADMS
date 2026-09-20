package rw.adms.presentation.users.dto;

import jakarta.validation.constraints.NotNull;
import rw.adms.domain.users.enums.Permission;

public record GrantPermissionRequest(

        @NotNull(message = "Permission is required")
        Permission permission
) {
}
