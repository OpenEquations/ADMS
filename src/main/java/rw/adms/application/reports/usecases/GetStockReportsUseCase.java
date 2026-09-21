package rw.adms.application.reports.usecases;

import rw.adms.domain.reports.StockReport;
import rw.adms.domain.reports.interfaces.StockReportRepository;

import java.util.List;

public class GetStockReportsUseCase {

    private final StockReportRepository stockReportRepository;

    public GetStockReportsUseCase(StockReportRepository stockReportRepository) {
        this.stockReportRepository = stockReportRepository;
    }

    public List<StockReport> execute() {
        return stockReportRepository.findAllOrderByGeneratedAtDesc();
    }
}
