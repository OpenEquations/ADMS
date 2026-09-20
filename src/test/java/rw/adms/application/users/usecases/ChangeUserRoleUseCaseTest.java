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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChangeUserRoleUseCaseTest {

    private static User user(Long id, UserRole role) {
        return User.reconstitute(id, "First", "Last", "user" + id + "@example.com", "hash", role, Set.of());
    }

    @Test
    void promotesUserToSuperAdmin() {

        UserRepository userRepository = mock(UserRepository.class);
        User target = user(1L, UserRole.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(target));

        ChangeUserRoleUseCase useCase = new ChangeUserRoleUseCase(userRepository);

        useCase.execute(1L, UserRole.SUPERADMIN, UserRole.SUPERADMIN);

        assertEquals(UserRole.SUPERADMIN, target.getRole());
        verify(userRepository).save(target);
    }

    @Test
    void rejectsChangeByNonSuperAdminActor() {

        UserRepository userRepository = mock(UserRepository.class);
        ChangeUserRoleUseCase useCase = new ChangeUserRoleUseCase(userRepository);

        assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(1L, UserRole.SUPERADMIN, UserRole.USER)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void rejectsDemotingTheLastSuperAdmin() {

        UserRepository userRepository = mock(UserRepository.class);
        User onlySuperAdmin = user(1L, UserRole.SUPERADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(onlySuperAdmin));
        when(userRepository.findAll()).thenReturn(List.of(onlySuperAdmin));

        ChangeUserRoleUseCase useCase = new ChangeUserRoleUseCase(userRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, UserRole.USER, UserRole.SUPERADMIN)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void allowsDemotingASuperAdminWhenAnotherOneRemains() {

        UserRepository userRepository = mock(UserRepository.class);
        User target = user(1L, UserRole.SUPERADMIN);
        User anotherSuperAdmin = user(2L, UserRole.SUPERADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(target));
        when(userRepository.findAll()).thenReturn(List.of(target, anotherSuperAdmin));

        ChangeUserRoleUseCase useCase = new ChangeUserRoleUseCase(userRepository);

        useCase.execute(1L, UserRole.USER, UserRole.SUPERADMIN);

        assertEquals(UserRole.USER, target.getRole());
        verify(userRepository).save(target);
    }
}
