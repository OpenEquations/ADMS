package rw.adms.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rw.adms.infrastructure.persistence.entities.StockReportJpaEntity;

import java.util.List;

public interface SpringDataStockReportRepository extends JpaRepository<StockReportJpaEntity, Long> {

    List<StockReportJpaEntity> findAllByOrderByGeneratedAtDesc();
}
