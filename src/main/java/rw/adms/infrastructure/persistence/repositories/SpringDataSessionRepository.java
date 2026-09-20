package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.SessionJpaEntity;

import java.util.Optional;

public interface SpringDataSessionRepository extends JpaRepository<SessionJpaEntity, Long> {

    Optional<SessionJpaEntity> findByToken(String token);

    void deleteByToken(String token);
}
