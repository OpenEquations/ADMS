package rw.adms.application.auth.usecases;

import rw.adms.domain.auth.interfaces.SessionRepository;

public class LogoutUseCase {

    private final SessionRepository sessionRepository;

    public LogoutUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void execute(String token) {

        if (token == null || token.isBlank()) {
            return;
        }

        sessionRepository.deleteByToken(token);
    }
}
