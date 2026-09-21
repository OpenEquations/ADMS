package rw.adms.application.reports.usecases;

import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.reports.StockReport;
import rw.adms.domain.reports.interfaces.StockReportRepository;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateStockReportUseCase {

    private static final int LOW_HEALTH_THRESHOLD = 30;

    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final CompanyRepository companyRepository;
    private final TenderRepository tenderRepository;
    private final StockReportRepository stockReportRepository;

    public GenerateStockReportUseCase(
            ItemRepository itemRepository,
            WarehouseRepository warehouseRepository,
            CompanyRepository companyRepository,
            TenderRepository tenderRepository,
            StockReportRepository stockReportRepository
    ) {
        this.itemRepository = itemRepository;
        this.warehouseRepository = warehouseRepository;
        this.companyRepository = companyRepository;
        this.tenderRepository = tenderRepository;
        this.stockReportRepository = stockReportRepository;
    }

    public StockReport execute(String generatedByName) {

        List<Item> items = itemRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();

        Set<Long> allocatedItemIds = warehouses.stream()
                .flatMap(warehouse -> warehouse.getItems().stream())
                .map(item -> item.getItemId().getValue())
                .collect(Collectors.toCollection(HashSet::new));

        int unallocated = (int) items.stream()
                .filter(item -> !allocatedItemIds.contains(item.getItemId().getValue()))
                .count();

        List<StockReport.ItemCount> itemsByStatus = items.stream()
                .collect(Collectors.groupingBy(item -> item.getItemStatus().name(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new StockReport.ItemCount(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(StockReport.ItemCount::key))
                .toList();

        List<StockReport.ItemCount> itemsByType = items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getItemType() == null ? "UNSPECIFIED" : item.getItemType().name(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> new StockReport.ItemCount(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(StockReport.ItemCount::key))
                .toList();

        List<StockReport.WarehouseItemCount> itemsByWarehouse = warehouses.stream()
                .map(warehouse -> new StockReport.WarehouseItemCount(
                        warehouse.getName().getValue(),
                        warehouse.getItems().size()
                ))
                .sorted(Comparator.comparing(StockReport.WarehouseItemCount::warehouseName))
                .toList();

        List<StockReport.LowHealthItem> lowHealthItems = items.stream()
                .filter(item -> item.getItemHealth().getValue() <= LOW_HEALTH_THRESHOLD)
                .map(item -> new StockReport.LowHealthItem(
                        item.getItemId().getValue(),
                        item.getItemName(),
                        item.getItemHealth().getValue(),
                        item.getItemStatus().name()
                ))
                .sorted(Comparator.comparingInt(StockReport.LowHealthItem::health))
                .toList();

        StockReport report = new StockReport(
                null,
                LocalDateTime.now(),
                generatedByName,
                items.size(),
                warehouses.size(),
                companyRepository.findAll().size(),
                tenderRepository.findAll().size(),
                unallocated,
                itemsByStatus,
                itemsByType,
                itemsByWarehouse,
                lowHealthItems
        );

        return stockReportRepository.save(report);
    }
}
