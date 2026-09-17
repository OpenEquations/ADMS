package rw.adms.domain.warehouses;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.warehouses.vo.WarehouseId;
import rw.adms.domain.warehouses.vo.WarehouseName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Warehouse {

    private WarehouseId id;
    private WarehouseName name;

    private final List<Item> items = new ArrayList<>();

    public Warehouse(String name) {
        this.name = new WarehouseName(name);
    }

    private Warehouse(
            WarehouseId id,
            WarehouseName name,
            List<Item> items
    ) {
        this.id = id;
        this.name = name;
        this.items.addAll(items);
    }

    public static Warehouse reconstitute(
            Long id,
            String name,
            List<Item> items
    ) {
        return new Warehouse(
                new WarehouseId(id),
                new WarehouseName(name),
                items
        );
    }

    public WarehouseId getId() {
        return id;
    }

    public WarehouseName getName() {
        return name;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void changeName(String name) {
        this.name = new WarehouseName(name);
    }

    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (containsItem(item.getItemId())) {
            throw new IllegalArgumentException(
                    "Item already exists in this warehouse"
            );
        }

        items.add(item);
    }

    public void removeItem(ItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null"
            );
        }

        boolean removed = items.removeIf(
                item -> item.getItemId().equals(itemId)
        );

        if (!removed) {
            throw new IllegalArgumentException(
                    "Item does not exist in this warehouse"
            );
        }
    }

    public Item getItem(ItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException(
                    "Item ID cannot be null"
            );
        }

        return items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Item does not exist in this warehouse"
                ));
    }

    public boolean containsItem(ItemId itemId) {
        if (itemId == null) {
            return false;
        }

        return items.stream()
                .anyMatch(item -> item.getItemId().equals(itemId));
    }
}