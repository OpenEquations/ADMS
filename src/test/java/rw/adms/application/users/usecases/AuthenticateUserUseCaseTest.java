package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;
import rw.adms.infrastructure.security.BCryptPasswordHasher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticateUserUseCaseTest {

    private final PasswordHasher passwordHasher = new BCryptPasswordHasher();

    @Test
    void succeedsWithMatchingHashedPassword() {

        UserRepository userRepository = mock(UserRepository.class);

        User user = User.reconstitute(
                1L, "Bonheur", "Iradukunda", "bonheur@example.com",
                passwordHasher.hash("password123")
        );

        when(userRepository.findByEmail("bonheur@example.com")).thenReturn(Optional.of(user));

        AuthenticateUserUseCase useCase = new AuthenticateUserUseCase(userRepository, passwordHasher);

        Optional<User> result = useCase.execute("bonheur@example.com", "password123");

        assertTrue(result.isPresent());
    }

    @Test
    void failsWithWrongPassword() {

        UserRepository userRepository = mock(UserRepository.class);

        User user = User.reconstitute(
                1L, "Bonheur", "Iradukunda", "bonheur@example.com",
                passwordHasher.hash("password123")
        );

        when(userRepository.findByEmail("bonheur@example.com")).thenReturn(Optional.of(user));

        AuthenticateUserUseCase useCase = new AuthenticateUserUseCase(userRepository, passwordHasher);

        Optional<User> result = useCase.execute("bonheur@example.com", "wrongPassword");

        assertFalse(result.isPresent());
    }

    @Test
    void failsWhenEmailUnknown() {

        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        AuthenticateUserUseCase useCase = new AuthenticateUserUseCase(userRepository, passwordHasher);

        Optional<User> result = useCase.execute("nobody@example.com", "password123");

        assertFalse(result.isPresent());
    }
}
