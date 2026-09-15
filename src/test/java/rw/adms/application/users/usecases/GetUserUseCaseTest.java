package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetUserUseCaseTest {

    @Test
    void shouldReturnUser() {

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

        GetUserUseCase useCase =
                new GetUserUseCase(userRepository);

        // Act
        Optional<User> result = useCase.execute(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenUserDoesNotExist() {

        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        GetUserUseCase useCase =
                new GetUserUseCase(userRepository);

        Optional<User> result = useCase.execute(1L);

        assertTrue(result.isEmpty());

        verify(userRepository).findById(1L);
    }
}