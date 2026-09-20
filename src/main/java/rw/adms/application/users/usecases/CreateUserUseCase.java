package rw.adms.application.users.usecases;

import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.UserRole;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * The very first user ever created for a deployment becomes the
     * superadmin automatically - there is no one else who could have
     * authorized it. Every user created after that requires a superadmin
     * actor and starts out as a plain USER with no permissions.
     */
    public User execute(
            String firstName,
            String lastName,
            String email,
            String password,
            UserRole actingRole
    ) {

        boolean isBootstrap = !userRepository.existsAny();

        UserRole role;

        if (isBootstrap) {
            role = UserRole.SUPERADMIN;
        } else {
            if (actingRole != UserRole.SUPERADMIN) {
                throw new ForbiddenException("Only a superadmin can add users");
            }
            role = UserRole.USER;
        }

        User user = new User(
                firstName,
                lastName,
                email,
                passwordHasher.hash(password),
                role
        );

        return userRepository.save(user);
    }
}
