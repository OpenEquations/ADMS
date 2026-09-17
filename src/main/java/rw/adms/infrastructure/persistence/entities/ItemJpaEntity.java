package rw.adms.infrastructure.persistence.entities;

import jakarta.persistence.*;
import rw.adms.domain.items.enums.ItemStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
public class ItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String itemDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus itemStatus;

    @Column(nullable = false)
    private Integer itemHealth;

    private LocalDate dateBought;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private BigDecimal soldAtAmount;

    private String soldAtCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sold_to_company_id")
    private CompanyJpaEntity soldTo;

    private BigDecimal repairCostAmount;

    private String repairCostCurrency;

    protected ItemJpaEntity() {
    }

    public ItemJpaEntity(
            String itemName,
            String itemDescription,
            ItemStatus itemStatus,
            Integer itemHealth,
            LocalDate dateBought,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemStatus = itemStatus;
        this.itemHealth = itemHealth;
        this.dateBought = dateBought;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public Integer getItemHealth() {
        return itemHealth;
    }

    public LocalDate getDateBought() {
        return dateBought;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public BigDecimal getSoldAtAmount() {
        return soldAtAmount;
    }

    public String getSoldAtCurrency() {
        return soldAtCurrency;
    }

    public CompanyJpaEntity getSoldTo() {
        return soldTo;
    }

    public BigDecimal getRepairCostAmount() {
        return repairCostAmount;
    }

    public String getRepairCostCurrency() {
        return repairCostCurrency;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public void setItemHealth(Integer itemHealth) {
        this.itemHealth = itemHealth;
    }

    public void setDateBought(LocalDate dateBought) {
        this.dateBought = dateBought;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSoldAtAmount(BigDecimal soldAtAmount) {
        this.soldAtAmount = soldAtAmount;
    }

    public void setSoldAtCurrency(String soldAtCurrency) {
        this.soldAtCurrency = soldAtCurrency;
    }

    public void setSoldTo(CompanyJpaEntity soldTo) {
        this.soldTo = soldTo;
    }

    public void setRepairCostAmount(BigDecimal repairCostAmount) {
        this.repairCostAmount = repairCostAmount;
    }

    public void setRepairCostCurrency(String repairCostCurrency) {
        this.repairCostCurrency = repairCostCurrency;
    }
}