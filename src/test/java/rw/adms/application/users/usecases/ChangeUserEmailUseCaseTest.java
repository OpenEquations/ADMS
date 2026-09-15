package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChangeUserEmailUseCaseTest {

    @Test
    void shouldChangeUserEmail() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "old@example.com",
                "password123"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangeUserEmailUseCase useCase =
                new ChangeUserEmailUseCase(userRepository);

        // Act
        useCase.execute(
                1L,
                "new@example.com"
        );

        // Assert
        assertEquals(
                "new@example.com",
                user.getEmail()
        );

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ChangeUserEmailUseCase useCase =
                new ChangeUserEmailUseCase(userRepository);

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        1L,
                        "new@example.com"
                )
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }
}