package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    private static User user(Long id, UserRole role) {
        return User.reconstitute(
                id, "First", "Last", "user" + id + "@example.com", "hash", role, Set.of()
        );
    }

    @Test
    void shouldDeleteUser() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User target = user(1L, UserRole.USER);
        User anotherSuperAdmin = user(2L, UserRole.SUPERADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(target));
        when(userRepository.findAll()).thenReturn(List.of(target, anotherSuperAdmin));

        DeleteUserUseCase useCase = new DeleteUserUseCase(userRepository);

        // Act
        useCase.execute(1L, UserRole.SUPERADMIN);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        DeleteUserUseCase useCase = new DeleteUserUseCase(userRepository);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, UserRole.SUPERADMIN)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void shouldRejectDeletionByNonSuperAdminActor() {

        UserRepository userRepository = mock(UserRepository.class);

        DeleteUserUseCase useCase = new DeleteUserUseCase(userRepository);

        assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(1L, UserRole.USER)
        );

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void shouldRejectDeletingTheLastSuperAdmin() {

        UserRepository userRepository = mock(UserRepository.class);

        User onlySuperAdmin = user(1L, UserRole.SUPERADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(onlySuperAdmin));
        when(userRepository.findAll()).thenReturn(List.of(onlySuperAdmin));

        DeleteUserUseCase useCase = new DeleteUserUseCase(userRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, UserRole.SUPERADMIN)
        );

        verify(userRepository, never()).deleteById(any());
    }
}
