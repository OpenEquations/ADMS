package rw.adms.application.reports.usecases;

import rw.adms.domain.reports.StockReport;
import rw.adms.domain.reports.interfaces.StockReportRepository;

public class GetStockReportUseCase {

    private final StockReportRepository stockReportRepository;

    public GetStockReportUseCase(StockReportRepository stockReportRepository) {
        this.stockReportRepository = stockReportRepository;
    }

    public StockReport execute(Long id) {
        return stockReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }
}
