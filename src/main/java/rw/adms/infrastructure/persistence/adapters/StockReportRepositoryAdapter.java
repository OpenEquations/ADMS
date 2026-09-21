package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.reports.StockReport;
import rw.adms.domain.reports.interfaces.StockReportRepository;
import rw.adms.infrastructure.persistence.entities.StockReportJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataStockReportRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class StockReportRepositoryAdapter implements StockReportRepository {

    private final SpringDataStockReportRepository repository;
    private final ObjectMapper objectMapper;

    public StockReportRepositoryAdapter(
            SpringDataStockReportRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    private record Details(
            List<StockReport.ItemCount> itemsByStatus,
            List<StockReport.ItemCount> itemsByType,
            List<StockReport.WarehouseItemCount> itemsByWarehouse,
            List<StockReport.LowHealthItem> lowHealthItems
    ) {
    }

    @Override
    public StockReport save(StockReport report) {

        String detailsJson = objectMapper.writeValueAsString(new Details(
                report.getItemsByStatus(),
                report.getItemsByType(),
                report.getItemsByWarehouse(),
                report.getLowHealthItems()
        ));

        StockReportJpaEntity entity = new StockReportJpaEntity(
                report.getGeneratedAt(),
                report.getGeneratedByName(),
                report.getTotalItems(),
                report.getTotalWarehouses(),
                report.getTotalCompanies(),
                report.getTotalTenders(),
                report.getUnallocatedItems(),
                detailsJson
        );

        StockReportJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<StockReport> findById(Long id) {

        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<StockReport> findAllOrderByGeneratedAtDesc() {

        return repository.findAllByOrderByGeneratedAtDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private StockReport toDomain(StockReportJpaEntity entity) {

        Details details = objectMapper.readValue(entity.getDetailsJson(), Details.class);

        return new StockReport(
                entity.getId(),
                entity.getGeneratedAt(),
                entity.getGeneratedByName(),
                entity.getTotalItems(),
                entity.getTotalWarehouses(),
                entity.getTotalCompanies(),
                entity.getTotalTenders(),
                entity.getUnallocatedItems(),
                details.itemsByStatus(),
                details.itemsByType(),
                details.itemsByWarehouse(),
                details.lowHealthItems()
        );
    }
}
