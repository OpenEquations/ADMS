package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.WarehouseJpaEntity;

import java.util.Optional;

public interface SpringDataWarehouseRepository
        extends JpaRepository<WarehouseJpaEntity, Long> {

    Optional<WarehouseJpaEntity> findByName(String name);

    boolean existsByName(String name);
}