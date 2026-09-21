package rw.adms.domain.reports;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A frozen snapshot of the stock's state at the moment it was generated -
 * not a live view. Re-opening an old report should show what the stock
 * looked like on that date, even if items have since changed or been
 * deleted, which is why the breakdown is stored rather than recomputed.
 */
public class StockReport {

    public record ItemCount(String key, long count) {
    }

    public record WarehouseItemCount(String warehouseName, long itemCount) {
    }

    public record LowHealthItem(Long itemId, String itemName, int health, String status) {
    }

    private final Long id;
    private final LocalDateTime generatedAt;
    private final String generatedByName;
    private final int totalItems;
    private final int totalWarehouses;
    private final int totalCompanies;
    private final int totalTenders;
    private final int unallocatedItems;
    private final List<ItemCount> itemsByStatus;
    private final List<ItemCount> itemsByType;
    private final List<WarehouseItemCount> itemsByWarehouse;
    private final List<LowHealthItem> lowHealthItems;

    public StockReport(
            Long id,
            LocalDateTime generatedAt,
            String generatedByName,
            int totalItems,
            int totalWarehouses,
            int totalCompanies,
            int totalTenders,
            int unallocatedItems,
            List<ItemCount> itemsByStatus,
            List<ItemCount> itemsByType,
            List<WarehouseItemCount> itemsByWarehouse,
            List<LowHealthItem> lowHealthItems
    ) {
        this.id = id;
        this.generatedAt = generatedAt;
        this.generatedByName = generatedByName;
        this.totalItems = totalItems;
        this.totalWarehouses = totalWarehouses;
        this.totalCompanies = totalCompanies;
        this.totalTenders = totalTenders;
        this.unallocatedItems = unallocatedItems;
        this.itemsByStatus = itemsByStatus;
        this.itemsByType = itemsByType;
        this.itemsByWarehouse = itemsByWarehouse;
        this.lowHealthItems = lowHealthItems;
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

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalWarehouses() {
        return totalWarehouses;
    }

    public int getTotalCompanies() {
        return totalCompanies;
    }

    public int getTotalTenders() {
        return totalTenders;
    }

    public int getUnallocatedItems() {
        return unallocatedItems;
    }

    public List<ItemCount> getItemsByStatus() {
        return itemsByStatus;
    }

    public List<ItemCount> getItemsByType() {
        return itemsByType;
    }

    public List<WarehouseItemCount> getItemsByWarehouse() {
        return itemsByWarehouse;
    }

    public List<LowHealthItem> getLowHealthItems() {
        return lowHealthItems;
    }
}
