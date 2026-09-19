package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetUsersUseCaseTest {

    @Test
    void shouldReturnAllUsers() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123"
        );

        when(userRepository.findAll()).thenReturn(List.of(user));

        GetUsersUseCase useCase = new GetUsersUseCase(userRepository);

        // Act
        List<User> result = useCase.execute();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(user));

        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {

        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findAll()).thenReturn(List.of());

        GetUsersUseCase useCase = new GetUsersUseCase(userRepository);

        List<User> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
