package rw.adms.application.users.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.Permission;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RevokePermissionUseCaseTest {

    @Test
    void revokesPermissionWhenActorIsSuperAdmin() {

        UserRepository userRepository = mock(UserRepository.class);
        User target = User.reconstitute(
                1L, "First", "Last", "user@example.com", "hash",
                UserRole.USER, Set.of(Permission.MANAGE_ITEMS)
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(target));

        RevokePermissionUseCase useCase = new RevokePermissionUseCase(userRepository);

        useCase.execute(1L, Permission.MANAGE_ITEMS, UserRole.SUPERADMIN);

        assertFalse(target.hasPermission(Permission.MANAGE_ITEMS));
        verify(userRepository).save(target);
    }

    @Test
    void rejectsRevokeByNonSuperAdminActor() {

        UserRepository userRepository = mock(UserRepository.class);
        RevokePermissionUseCase useCase = new RevokePermissionUseCase(userRepository);

        assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(1L, Permission.MANAGE_ITEMS, UserRole.USER)
        );

        verify(userRepository, never()).save(any());
    }
}
