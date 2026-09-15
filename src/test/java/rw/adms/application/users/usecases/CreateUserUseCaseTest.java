package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    @Test
    void shouldCreateUser() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        CreateUserUseCase useCase =
                new CreateUserUseCase(userRepository);

        // Act
        useCase.execute(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123"
        );

        // Assert
        verify(userRepository).save(any(User.class));
    }
}