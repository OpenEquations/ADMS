package rw.adms.presentation.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.auth.usecases.CreateSessionUseCase;
import rw.adms.application.auth.usecases.LogoutUseCase;
import rw.adms.application.users.usecases.AuthenticateUserUseCase;
import rw.adms.domain.auth.Session;
import rw.adms.domain.users.User;
import rw.adms.presentation.auth.dto.LoginRequest;
import rw.adms.presentation.auth.dto.LoginResponse;
import rw.adms.presentation.common.ApiError;
import rw.adms.presentation.users.dto.UserResponse;

import java.util.Optional;

/**
 * Passwords are hashed (BCrypt) and login issues an opaque bearer session
 * token - see CreateSessionUseCase / ValidateSessionUseCase / the
 * {@code @CurrentUser} resolver for how it's verified on later requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final CreateSessionUseCase createSessionUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(
            AuthenticateUserUseCase authenticateUserUseCase,
            CreateSessionUseCase createSessionUseCase,
            LogoutUseCase logoutUseCase
    ) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.createSessionUseCase = createSessionUseCase;
        this.logoutUseCase = logoutUseCase;
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

        Session session = createSessionUseCase.execute(user.get().getId());

        return ResponseEntity.ok(
                new LoginResponse(session.getToken(), UserResponse.from(user.get()))
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        String token = extractToken(authorization);
        logoutUseCase.execute(token);

        return ResponseEntity.noContent().build();
    }

    private String extractToken(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
    }
}
