package rw.adms.domain.items;

import rw.adms.domain.companies.Company;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.shared.vo.Money;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Item {

    private ItemId itemId;
    private String itemName;
    private String itemDescription;
    private ItemStatus itemStatus;
    private ItemType itemType;
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
            ItemType itemType,
            ItemHealth itemHealth,
            LocalDate dateBought
    ) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemStatus = itemStatus;
        this.itemType = itemType;
        this.itemHealth = itemHealth;
        this.dateBought = dateBought;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private Item(
            ItemId itemId,
            String itemName,
            String itemDescription,
            ItemStatus itemStatus,
            ItemType itemType,
            ItemHealth itemHealth,
            LocalDate dateBought,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Money soldAt,
            Company soldTo,
            Money repairCost
    ) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemStatus = itemStatus;
        this.itemType = itemType;
        this.itemHealth = itemHealth;
        this.dateBought = dateBought;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.soldAt = soldAt;
        this.soldTo = soldTo;
        this.repairCost = repairCost;
    }

    public static Item reconstitute(
            Long id,
            String itemName,
            String itemDescription,
            ItemStatus itemStatus,
            ItemType itemType,
            int itemHealth,
            LocalDate dateBought,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Money soldAt,
            Company soldTo,
            Money repairCost
    ) {
        return new Item(
                new ItemId(id),
                itemName,
                itemDescription,
                itemStatus,
                itemType,
                new ItemHealth(itemHealth),
                dateBought,
                createdAt,
                updatedAt,
                soldAt,
                soldTo,
                repairCost
        );
    }

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

    public ItemType getItemType() {
        return itemType;
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

    public Money getRepairCost() {
        return repairCost;
    }

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

    public boolean updateItemType(ItemType itemType) {
        if (itemType == null) {
            return false;
        }

        this.itemType = itemType;
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
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}