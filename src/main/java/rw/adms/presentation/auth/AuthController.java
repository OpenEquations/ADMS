package rw.adms.presentation.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.users.usecases.AuthenticateUserUseCase;
import rw.adms.domain.users.User;
import rw.adms.presentation.auth.dto.LoginRequest;
import rw.adms.presentation.common.ApiError;
import rw.adms.presentation.users.dto.UserResponse;

import java.util.Optional;

/**
 * Demo-grade authentication: checks email/password against the users table
 * with a plain equality check (the domain layer stores passwords as-is,
 * there is no hashing yet). Good enough to gate the frontend's login screen,
 * not a substitute for real credential handling.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {

        Optional<User> user = authenticateUserUseCase.execute(request.email(), request.password());

        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiError.of(
                            HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                            "Invalid email or password"
                    ));
        }

        return ResponseEntity.ok(UserResponse.from(user.get()));
    }
}
