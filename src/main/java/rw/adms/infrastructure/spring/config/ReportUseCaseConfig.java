package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.reports.usecases.GenerateStockReportUseCase;
import rw.adms.application.reports.usecases.GetStockReportUseCase;
import rw.adms.application.reports.usecases.GetStockReportsUseCase;
import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.reports.interfaces.StockReportRepository;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

@Configuration
public class ReportUseCaseConfig {

    @Bean
    public GenerateStockReportUseCase generateStockReportUseCase(
            ItemRepository itemRepository,
            WarehouseRepository warehouseRepository,
            CompanyRepository companyRepository,
            TenderRepository tenderRepository,
            StockReportRepository stockReportRepository
    ) {
        return new GenerateStockReportUseCase(
                itemRepository, warehouseRepository, companyRepository, tenderRepository, stockReportRepository
        );
    }

    @Bean
    public GetStockReportsUseCase getStockReportsUseCase(StockReportRepository stockReportRepository) {
        return new GetStockReportsUseCase(stockReportRepository);
    }

    @Bean
    public GetStockReportUseCase getStockReportUseCase(StockReportRepository stockReportRepository) {
        return new GetStockReportUseCase(stockReportRepository);
    }
}
