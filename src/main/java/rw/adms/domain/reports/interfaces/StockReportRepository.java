package rw.adms.domain.reports.interfaces;

import rw.adms.domain.reports.StockReport;

import java.util.List;
import java.util.Optional;

public interface StockReportRepository {

    StockReport save(StockReport report);

    Optional<StockReport> findById(Long id);

    /**
     * Newest first - that's the order a report history list is read in.
     */
    List<StockReport> findAllOrderByGeneratedAtDesc();
}
