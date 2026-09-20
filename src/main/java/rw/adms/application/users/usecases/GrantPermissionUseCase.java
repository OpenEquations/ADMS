package rw.adms.application.users.usecases;

import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.Permission;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.interfaces.UserRepository;

public class GrantPermissionUseCase {

    private final UserRepository userRepository;

    public GrantPermissionUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId, Permission permission, UserRole actingRole) {

        if (actingRole != UserRole.SUPERADMIN) {
            throw new ForbiddenException("Only a superadmin can grant permissions");
        }

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        target.grantPermission(permission);

        userRepository.save(target);
    }
}
