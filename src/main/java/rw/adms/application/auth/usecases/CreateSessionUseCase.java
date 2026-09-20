package rw.adms.application.auth.usecases;

import rw.adms.domain.auth.Session;
import rw.adms.domain.auth.interfaces.SessionRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class CreateSessionUseCase {

    private static final int TOKEN_BYTES = 32;
    private static final long SESSION_DURATION_DAYS = 7;

    private final SessionRepository sessionRepository;
    private final SecureRandom random = new SecureRandom();

    public CreateSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session execute(Long userId) {

        byte[] bytes = new byte[TOKEN_BYTES];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        LocalDateTime now = LocalDateTime.now();

        Session session = new Session(
                token,
                userId,
                now.plusDays(SESSION_DURATION_DAYS),
                now
        );

        return sessionRepository.save(session);
    }
}
