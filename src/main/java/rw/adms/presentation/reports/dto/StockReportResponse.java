package rw.adms.presentation.reports.dto;

import rw.adms.domain.reports.StockReport;

import java.time.LocalDateTime;
import java.util.List;

public record StockReportResponse(
        Long id,
        LocalDateTime generatedAt,
        String generatedByName,
        int totalItems,
        int totalWarehouses,
        int totalCompanies,
        int totalTenders,
        int unallocatedItems,
        List<StockReport.ItemCount> itemsByStatus,
        List<StockReport.ItemCount> itemsByType,
        List<StockReport.WarehouseItemCount> itemsByWarehouse,
        List<StockReport.LowHealthItem> lowHealthItems
) {

    public static StockReportResponse from(StockReport report) {
        return new StockReportResponse(
                report.getId(),
                report.getGeneratedAt(),
                report.getGeneratedByName(),
                report.getTotalItems(),
                report.getTotalWarehouses(),
                report.getTotalCompanies(),
                report.getTotalTenders(),
                report.getUnallocatedItems(),
                report.getItemsByStatus(),
                report.getItemsByType(),
                report.getItemsByWarehouse(),
                report.getLowHealthItems()
        );
    }
}
