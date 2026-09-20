package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;
import rw.adms.infrastructure.security.BCryptPasswordHasher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ChangeUserPasswordUseCaseTest {

    private final PasswordHasher passwordHasher = new BCryptPasswordHasher();

    @Test
    void shouldChangeUserPassword() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "oldPassword"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangeUserPasswordUseCase useCase =
                new ChangeUserPasswordUseCase(userRepository, passwordHasher);

        // Act
        useCase.execute(1L, "newPassword");

        // Assert - stored value is a hash, not the raw password
        assertNotEquals("newPassword", user.getPassword());
        assertTrue(passwordHasher.matches("newPassword", user.getPassword()));

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ChangeUserPasswordUseCase useCase =
                new ChangeUserPasswordUseCase(userRepository, passwordHasher);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, "newPassword")
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }
}
