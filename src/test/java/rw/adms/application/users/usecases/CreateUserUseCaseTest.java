package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;
import rw.adms.infrastructure.security.BCryptPasswordHasher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    private final PasswordHasher passwordHasher = new BCryptPasswordHasher();

    @Test
    void firstUserEverCreatedBecomesSuperAdminWithNoActor() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsAny()).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateUserUseCase useCase =
                new CreateUserUseCase(userRepository, passwordHasher);

        // Act - no actor is possible yet, nobody is logged in
        User created = useCase.execute(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123",
                null
        );

        // Assert
        assertEquals(UserRole.SUPERADMIN, created.getRole());
        assertEquals("bonheur@example.com", created.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void subsequentUserRequiresSuperAdminActorAndDefaultsToUser() {

        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsAny()).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateUserUseCase useCase =
                new CreateUserUseCase(userRepository, passwordHasher);

        // Act
        User created = useCase.execute(
                "Joseph",
                "Uwase",
                "joseph@example.com",
                "password123",
                UserRole.SUPERADMIN
        );

        // Assert
        assertEquals(UserRole.USER, created.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void rejectsCreationByNonSuperAdminActor() {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsAny()).thenReturn(true);

        CreateUserUseCase useCase =
                new CreateUserUseCase(userRepository, passwordHasher);

        assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(
                        "Joseph",
                        "Uwase",
                        "joseph@example.com",
                        "password123",
                        UserRole.USER
                )
        );

        verify(userRepository, never()).save(any());
    }
}
