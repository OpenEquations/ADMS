package rw.adms.domain.auth.interfaces;

import rw.adms.domain.auth.Session;

import java.util.Optional;

public interface SessionRepository {

    Session save(Session session);

    Optional<Session> findByToken(String token);

    void deleteByToken(String token);
}
