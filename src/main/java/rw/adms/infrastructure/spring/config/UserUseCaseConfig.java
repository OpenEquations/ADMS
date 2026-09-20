package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.users.usecases.AuthenticateUserUseCase;
import rw.adms.application.users.usecases.ChangeUserEmailUseCase;
import rw.adms.application.users.usecases.ChangeUserNameUseCase;
import rw.adms.application.users.usecases.ChangeUserPasswordUseCase;
import rw.adms.application.users.usecases.ChangeUserRoleUseCase;
import rw.adms.application.users.usecases.CreateUserUseCase;
import rw.adms.application.users.usecases.DeleteUserUseCase;
import rw.adms.application.users.usecases.GetUserUseCase;
import rw.adms.application.users.usecases.GetUsersUseCase;
import rw.adms.application.users.usecases.GrantPermissionUseCase;
import rw.adms.application.users.usecases.RevokePermissionUseCase;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;

/**
 * Wires the users bounded context's use cases as Spring beans.
 * <p>
 * Use case classes live in the application layer and stay framework-free;
 * this configuration is where the infrastructure layer takes on the job of
 * assembling them with their (already Spring-managed) repository ports.
 */
@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new CreateUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    public GetUserUseCase getUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }

    @Bean
    public GetUsersUseCase getUsersUseCase(UserRepository userRepository) {
        return new GetUsersUseCase(userRepository);
    }

    @Bean
    public ChangeUserNameUseCase changeUserNameUseCase(UserRepository userRepository) {
        return new ChangeUserNameUseCase(userRepository);
    }

    @Bean
    public ChangeUserEmailUseCase changeUserEmailUseCase(UserRepository userRepository) {
        return new ChangeUserEmailUseCase(userRepository);
    }

    @Bean
    public ChangeUserPasswordUseCase changeUserPasswordUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        return new ChangeUserPasswordUseCase(userRepository, passwordHasher);
    }

    @Bean
    public ChangeUserRoleUseCase changeUserRoleUseCase(UserRepository userRepository) {
        return new ChangeUserRoleUseCase(userRepository);
    }

    @Bean
    public GrantPermissionUseCase grantPermissionUseCase(UserRepository userRepository) {
        return new GrantPermissionUseCase(userRepository);
    }

    @Bean
    public RevokePermissionUseCase revokePermissionUseCase(UserRepository userRepository) {
        return new RevokePermissionUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        return new AuthenticateUserUseCase(userRepository, passwordHasher);
    }
}
