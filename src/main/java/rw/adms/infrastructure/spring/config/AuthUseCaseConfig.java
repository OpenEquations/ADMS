package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.auth.usecases.CreateSessionUseCase;
import rw.adms.application.auth.usecases.LogoutUseCase;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
import rw.adms.domain.auth.interfaces.SessionRepository;
import rw.adms.domain.users.interfaces.UserRepository;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public CreateSessionUseCase createSessionUseCase(SessionRepository sessionRepository) {
        return new CreateSessionUseCase(sessionRepository);
    }

    @Bean
    public ValidateSessionUseCase validateSessionUseCase(
            SessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        return new ValidateSessionUseCase(sessionRepository, userRepository);
    }

    @Bean
    public LogoutUseCase logoutUseCase(SessionRepository sessionRepository) {
        return new LogoutUseCase(sessionRepository);
    }
}
