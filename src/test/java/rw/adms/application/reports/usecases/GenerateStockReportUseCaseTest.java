package rw.adms.application.reports.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.reports.StockReport;
import rw.adms.domain.reports.interfaces.StockReportRepository;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenerateStockReportUseCaseTest {

    @Test
    void computesBreakdownsAndPersistsSnapshot() {

        ItemRepository itemRepository = mock(ItemRepository.class);
        WarehouseRepository warehouseRepository = mock(WarehouseRepository.class);
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        TenderRepository tenderRepository = mock(TenderRepository.class);
        StockReportRepository stockReportRepository = mock(StockReportRepository.class);

        Item healthyItem = Item.reconstitute(
                1L, "Laptop", "Dell laptop", ItemStatus.IN_USE, ItemType.ELECTRONICS,
                80, LocalDate.now(), null, null, null, null, null
        );
        Item lowHealthItem = Item.reconstitute(
                2L, "Old Chair", "Worn office chair", ItemStatus.NO_LONGER_IN_USE, ItemType.FURNITURE,
                15, LocalDate.now(), null, null, null, null, null
        );

        Warehouse warehouse = Warehouse.reconstitute(1L, "Main Warehouse", List.of(healthyItem));

        when(itemRepository.findAll()).thenReturn(List.of(healthyItem, lowHealthItem));
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));
        when(companyRepository.findAll()).thenReturn(List.of());
        when(tenderRepository.findAll()).thenReturn(List.of());
        when(stockReportRepository.save(any(StockReport.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GenerateStockReportUseCase useCase = new GenerateStockReportUseCase(
                itemRepository, warehouseRepository, companyRepository, tenderRepository, stockReportRepository
        );

        StockReport report = useCase.execute("Joe LeBonheur");

        assertEquals(2, report.getTotalItems());
        assertEquals(1, report.getTotalWarehouses());
        assertEquals(1, report.getUnallocatedItems());
        assertEquals("Joe LeBonheur", report.getGeneratedByName());
        assertEquals(1, report.getLowHealthItems().size());
        assertEquals("Old Chair", report.getLowHealthItems().get(0).itemName());
        assertEquals(1, report.getItemsByWarehouse().size());
        assertEquals(1, report.getItemsByWarehouse().get(0).itemCount());
    }
}
