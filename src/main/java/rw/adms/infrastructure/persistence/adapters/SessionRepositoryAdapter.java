package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.auth.Session;
import rw.adms.domain.auth.interfaces.SessionRepository;
import rw.adms.infrastructure.persistence.entities.SessionJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataSessionRepository;

import java.util.Optional;

@Repository
public class SessionRepositoryAdapter implements SessionRepository {

    private final SpringDataSessionRepository repository;

    public SessionRepositoryAdapter(SpringDataSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Session save(Session session) {

        SessionJpaEntity entity = new SessionJpaEntity(
                session.getToken(),
                session.getUserId(),
                session.getExpiresAt(),
                session.getCreatedAt()
        );

        SessionJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Session> findByToken(String token) {

        return repository.findByToken(token).map(this::toDomain);
    }

    @Override
    public void deleteByToken(String token) {

        repository.deleteByToken(token);
    }

    private Session toDomain(SessionJpaEntity entity) {

        return new Session(
                entity.getToken(),
                entity.getUserId(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }
}
