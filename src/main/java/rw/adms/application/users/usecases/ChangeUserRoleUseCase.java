package rw.adms.application.users.usecases;

import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.interfaces.UserRepository;

public class ChangeUserRoleUseCase {

    private final UserRepository userRepository;

    public ChangeUserRoleUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId, UserRole newRole, UserRole actingRole) {

        if (actingRole != UserRole.SUPERADMIN) {
            throw new ForbiddenException("Only a superadmin can change user roles");
        }

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean demotingLastSuperAdmin = target.getRole() == UserRole.SUPERADMIN
                && newRole != UserRole.SUPERADMIN
                && isLastSuperAdmin();

        if (demotingLastSuperAdmin) {
            throw new IllegalArgumentException("Cannot remove the last superadmin");
        }

        target.changeRole(newRole);

        userRepository.save(target);
    }

    private boolean isLastSuperAdmin() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.SUPERADMIN)
                .count() <= 1;
    }
}
