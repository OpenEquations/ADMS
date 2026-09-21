package rw.adms.presentation.reports.dto;

import rw.adms.domain.reports.StockReport;

import java.time.LocalDateTime;

public record StockReportSummaryResponse(
        Long id,
        LocalDateTime generatedAt,
        String generatedByName,
        int totalItems
) {

    public static StockReportSummaryResponse from(StockReport report) {
        return new StockReportSummaryResponse(
                report.getId(),
                report.getGeneratedAt(),
                report.getGeneratedByName(),
                report.getTotalItems()
        );
    }
}
