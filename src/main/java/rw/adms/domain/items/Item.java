package rw.adms.domain.items;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.shared.vo.Money;
import rw.adms.domain.company.Company;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Item {

    private ItemId itemId;
    private String itemName;
    private String itemDescription;
    private ItemStatus itemStatus;
    private ItemHealth itemHealth;

    private LocalDate dateBought;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Money soldAt;
    private Company soldTo;
    private Money repairCost;

    public Item(
            String itemName,
            String itemDescription,
            ItemStatus itemStatus,
            ItemHealth itemHealth,
            LocalDate dateBought
    ) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemStatus = itemStatus;
        this.itemHealth = itemHealth;
        this.dateBought = dateBought;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // =========================
    // Getters
    // =========================

    public ItemId getItemId() {
        return itemId;
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

    public ItemHealth getItemHealth() {
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

    public Money getSoldAt() {
        return soldAt;
    }

    public Company getSoldTo() {
        return soldTo;
    }

    // =========================
    // Domain behavior
    // =========================

    public boolean editItemName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            return false;
        }

        this.itemName = itemName;
        touch();

        return true;
    }

    public boolean updateItemDescription(String itemDescription) {
        if (itemDescription == null || itemDescription.isBlank()) {
            return false;
        }

        this.itemDescription = itemDescription;
        touch();

        return true;
    }

    public boolean updateItemStatus(ItemStatus itemStatus) {
        if (itemStatus == null) {
            return false;
        }

        this.itemStatus = itemStatus;
        touch();

        return true;
    }

    public boolean updateItemHealth(int health) {
        this.itemHealth = new ItemHealth(health);
        touch();

        return true;
    }

    public boolean markAsSold(Money soldAt, Company soldTo) {
        if (soldAt == null || soldTo == null) {
            return false;
        }

        this.soldAt = soldAt;
        this.soldTo = soldTo;
        this.itemStatus = ItemStatus.SOLD;

        touch();

        return true;
    }

    public void setRepairCost(Money repairCost) {
        this.repairCost = repairCost;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}