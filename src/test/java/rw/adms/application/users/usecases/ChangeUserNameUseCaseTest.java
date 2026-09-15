package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChangeUserNameUseCaseTest {

    @Test
    void shouldChangeUserName() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangeUserNameUseCase useCase =
                new ChangeUserNameUseCase(userRepository);

        // Act
        useCase.execute(
                1L,
                "Joseph",
                "Iradukunda"
        );

        // Assert
        assertEquals("Joseph Iradukunda", user.getName());

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ChangeUserNameUseCase useCase =
                new ChangeUserNameUseCase(userRepository);

        // Act & Assert
        IllegalArgumentException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.execute(
                                1L,
                                "Joseph",
                                "Iradukunda"
                        )
                );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);

        // User wasn't found, therefore it shouldn't be saved.
        verify(userRepository, never()).save(any());
    }
}