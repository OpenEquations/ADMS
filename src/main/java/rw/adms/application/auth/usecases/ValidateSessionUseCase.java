package rw.adms.application.auth.usecases;

import rw.adms.domain.auth.Session;
import rw.adms.domain.auth.interfaces.SessionRepository;
import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class ValidateSessionUseCase {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ValidateSessionUseCase(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Empty means the token is missing, unknown, or expired - callers can't
     * tell which, and don't need to; every case means "not authenticated."
     */
    public Optional<User> execute(String token) {

        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        Optional<Session> session = sessionRepository.findByToken(token);

        if (session.isEmpty() || session.get().isExpired(LocalDateTime.now())) {
            return Optional.empty();
        }

        return userRepository.findById(session.get().getUserId());
    }
}
