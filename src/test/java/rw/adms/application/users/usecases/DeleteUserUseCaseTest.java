package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.interfaces.UserRepository;

import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    @Test
    void shouldDeleteUser() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        DeleteUserUseCase useCase =
                new DeleteUserUseCase(userRepository);

        // Act
        useCase.execute(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }
}