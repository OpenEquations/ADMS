package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.interfaces.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    @Test
    void shouldDeleteUser() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.existsById(1L)).thenReturn(true);

        DeleteUserUseCase useCase =
                new DeleteUserUseCase(userRepository);

        // Act
        useCase.execute(1L);

        // Assert
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.existsById(1L)).thenReturn(false);

        DeleteUserUseCase useCase =
                new DeleteUserUseCase(userRepository);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never()).deleteById(any());
    }
}
