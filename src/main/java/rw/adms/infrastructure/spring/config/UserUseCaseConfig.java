package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.users.usecases.ChangeUserEmailUseCase;
import rw.adms.application.users.usecases.ChangeUserNameUseCase;
import rw.adms.application.users.usecases.ChangeUserPasswordUseCase;
import rw.adms.application.users.usecases.CreateUserUseCase;
import rw.adms.application.users.usecases.DeleteUserUseCase;
import rw.adms.application.users.usecases.GetUserUseCase;
import rw.adms.application.users.usecases.GetUsersUseCase;
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
    public CreateUserUseCase createUserUseCase(UserRepository userRepository) {
        return new CreateUserUseCase(userRepository);
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
    public ChangeUserPasswordUseCase changeUserPasswordUseCase(UserRepository userRepository) {
        return new ChangeUserPasswordUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserUseCase(userRepository);
    }
}
