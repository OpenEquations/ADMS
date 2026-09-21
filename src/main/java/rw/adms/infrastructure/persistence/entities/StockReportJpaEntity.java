package rw.adms.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * The variable-shaped breakdown (by status, by type, by warehouse, low
 * health items) is stored as one JSON blob rather than several child
 * tables - it's write-once, read-whole, and never queried by its contents,
 * so a relational shape would only add ceremony. See
 * {@code StockReportRepositoryAdapter} for the (de)serialization.
 */
@Entity
@Table(name = "stock_reports")
public class StockReportJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private String generatedByName;

    @Column(nullable = false)
    private Integer totalItems;

    @Column(nullable = false)
    private Integer totalWarehouses;

    @Column(nullable = false)
    private Integer totalCompanies;

    @Column(nullable = false)
    private Integer totalTenders;

    @Column(nullable = false)
    private Integer unallocatedItems;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String detailsJson;

    protected StockReportJpaEntity() {
    }

    public StockReportJpaEntity(
            LocalDateTime generatedAt,
            String generatedByName,
            Integer totalItems,
            Integer totalWarehouses,
            Integer totalCompanies,
            Integer totalTenders,
            Integer unallocatedItems,
            String detailsJson
    ) {
        this.generatedAt = generatedAt;
        this.generatedByName = generatedByName;
        this.totalItems = totalItems;
        this.totalWarehouses = totalWarehouses;
        this.totalCompanies = totalCompanies;
        this.totalTenders = totalTenders;
        this.unallocatedItems = unallocatedItems;
        this.detailsJson = detailsJson;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public String getGeneratedByName() {
        return generatedByName;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public Integer getTotalWarehouses() {
        return totalWarehouses;
    }

    public Integer getTotalCompanies() {
        return totalCompanies;
    }

    public Integer getTotalTenders() {
        return totalTenders;
    }

    public Integer getUnallocatedItems() {
        return unallocatedItems;
    }

    public String getDetailsJson() {
        return detailsJson;
    }
}
