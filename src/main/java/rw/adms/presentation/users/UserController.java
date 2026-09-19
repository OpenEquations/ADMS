package rw.adms.presentation.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
import rw.adms.application.users.usecases.CreateUserUseCase;
import rw.adms.application.users.usecases.DeleteUserUseCase;
import rw.adms.application.users.usecases.GetUserUseCase;
import rw.adms.application.users.usecases.GetUsersUseCase;
import rw.adms.domain.users.User;
import rw.adms.presentation.users.dto.ChangeUserEmailRequest;
import rw.adms.presentation.users.dto.ChangeUserNameRequest;
import rw.adms.presentation.users.dto.ChangeUserPasswordRequest;
import rw.adms.presentation.users.dto.CreateUserRequest;
import rw.adms.presentation.users.dto.UserResponse;

import java.util.List;

/**
 * HTTP entry point for the users bounded context. Translates requests into
 * use case calls and use case results into response DTOs - no business logic
 * lives here.
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
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetUserUseCase getUserUseCase,
            GetUsersUseCase getUsersUseCase,
            ChangeUserNameUseCase changeUserNameUseCase,
            ChangeUserEmailUseCase changeUserEmailUseCase,
            ChangeUserPasswordUseCase changeUserPasswordUseCase,
            DeleteUserUseCase deleteUserUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getUsersUseCase = getUsersUseCase;
        this.changeUserNameUseCase = changeUserNameUseCase;
        this.changeUserEmailUseCase = changeUserEmailUseCase;
        this.changeUserPasswordUseCase = changeUserPasswordUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateUserRequest request) {

        createUserUseCase.execute(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {

        User user = getUserUseCase.execute(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserNameRequest request
    ) {
        changeUserNameUseCase.execute(id, request.firstName(), request.lastName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Void> changeEmail(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserEmailRequest request
    ) {
        changeUserEmailUseCase.execute(id, request.email());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserPasswordRequest request
    ) {
        changeUserPasswordUseCase.execute(id, request.password());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
