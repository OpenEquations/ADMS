package rw.adms.presentation.users;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.users.usecases.ChangeUserEmailUseCase;
import rw.adms.application.users.usecases.ChangeUserNameUseCase;
import rw.adms.application.users.usecases.ChangeUserPasswordUseCase;
import rw.adms.application.users.usecases.ChangeUserRoleUseCase;
import rw.adms.application.users.usecases.CreateUserUseCase;
import rw.adms.application.users.usecases.DeleteUserUseCase;
import rw.adms.application.users.usecases.GetUserUseCase;
import rw.adms.application.users.usecases.GetUsersUseCase;
import rw.adms.application.users.usecases.GrantPermissionUseCase;
import rw.adms.application.users.usecases.RevokePermissionUseCase;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.Permission;
import rw.adms.presentation.security.CurrentUser;
import rw.adms.presentation.users.dto.ChangeUserEmailRequest;
import rw.adms.presentation.users.dto.ChangeUserNameRequest;
import rw.adms.presentation.users.dto.ChangeUserPasswordRequest;
import rw.adms.presentation.users.dto.ChangeUserRoleRequest;
import rw.adms.presentation.users.dto.CreateUserRequest;
import rw.adms.presentation.users.dto.GrantPermissionRequest;
import rw.adms.presentation.users.dto.UserResponse;

import java.net.URI;
import java.util.List;

/**
 * HTTP entry point for the users bounded context. Translates requests into
 * use case calls and use case results into response DTOs - no business logic
 * lives here.
 * <p>
 * {@code @CurrentUser} resolves the caller from their bearer token; use
 * cases that mutate role/permissions/membership take the caller's role and
 * decide there whether the action is allowed (superadmin-only, except the
 * very first user ever created, which needs no actor to exist yet).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final ChangeUserNameUseCase changeUserNameUseCase;
    private final ChangeUserEmailUseCase changeUserEmailUseCase;
    private final ChangeUserPasswordUseCase changeUserPasswordUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final GrantPermissionUseCase grantPermissionUseCase;
    private final RevokePermissionUseCase revokePermissionUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetUserUseCase getUserUseCase,
            GetUsersUseCase getUsersUseCase,
            ChangeUserNameUseCase changeUserNameUseCase,
            ChangeUserEmailUseCase changeUserEmailUseCase,
            ChangeUserPasswordUseCase changeUserPasswordUseCase,
            ChangeUserRoleUseCase changeUserRoleUseCase,
            GrantPermissionUseCase grantPermissionUseCase,
            RevokePermissionUseCase revokePermissionUseCase,
            DeleteUserUseCase deleteUserUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getUsersUseCase = getUsersUseCase;
        this.changeUserNameUseCase = changeUserNameUseCase;
        this.changeUserEmailUseCase = changeUserEmailUseCase;
        this.changeUserPasswordUseCase = changeUserPasswordUseCase;
        this.changeUserRoleUseCase = changeUserRoleUseCase;
        this.grantPermissionUseCase = grantPermissionUseCase;
        this.revokePermissionUseCase = revokePermissionUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @CurrentUser(required = false) User actingUser,
            @Valid @RequestBody CreateUserRequest request
    ) {
        User user = createUserUseCase.execute(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                actingUser == null ? null : actingUser.getRole()
        );

        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {

        List<UserResponse> users = getUsersUseCase.execute()
                .stream()
                .map(UserResponse::from)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable("id") Long id) {

        User user = getUserUseCase.execute(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> changeName(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeUserNameRequest request
    ) {
        changeUserNameUseCase.execute(id, request.firstName(), request.lastName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Void> changeEmail(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeUserEmailRequest request
    ) {
        changeUserEmailUseCase.execute(id, request.email());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeUserPasswordRequest request
    ) {
        changeUserPasswordUseCase.execute(id, request.password());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(
            @CurrentUser User actingUser,
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeUserRoleRequest request
    ) {
        changeUserRoleUseCase.execute(id, request.role(), actingUser.getRole());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<Void> grantPermission(
            @CurrentUser User actingUser,
            @PathVariable("id") Long id,
            @Valid @RequestBody GrantPermissionRequest request
    ) {
        grantPermissionUseCase.execute(id, request.permission(), actingUser.getRole());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permissions/{permission}")
    public ResponseEntity<Void> revokePermission(
            @CurrentUser User actingUser,
            @PathVariable("id") Long id,
            @PathVariable("permission") Permission permission
    ) {
        revokePermissionUseCase.execute(id, permission, actingUser.getRole());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @CurrentUser User actingUser,
            @PathVariable("id") Long id
    ) {
        deleteUserUseCase.execute(id, actingUser.getRole());
        return ResponseEntity.noContent().build();
    }
}
