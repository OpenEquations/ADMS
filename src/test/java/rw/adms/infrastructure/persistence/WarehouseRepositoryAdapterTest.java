package rw.adms.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;
import rw.adms.infrastructure.spring.AdmsApplication;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AdmsApplication.class)
class WarehouseRepositoryAdapterTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldSaveWarehouseWithItems() {

        // Arrange
        Item item = new Item(
                "Desktop Computer",
                "HP desktop computer",
                ItemStatus.NEW,
                ItemType.ELECTRONICS,
                new ItemHealth(9),
                LocalDate.now()
        );

        Item savedItem =
                itemRepository.save(item);

        Warehouse warehouse =
                new Warehouse("Main Warehouse");

        warehouse.addItem(savedItem);

        // Act
        Warehouse savedWarehouse =
                warehouseRepository.save(warehouse);

        // Assert
        assertNotNull(
                savedWarehouse
        );

        assertNotNull(
                savedWarehouse.getId()
        );

        assertEquals(
                "Main Warehouse",
                savedWarehouse.getName().getValue()
        );

        assertEquals(
                1,
                savedWarehouse.getItems().size()
        );

        assertEquals(
                savedItem.getItemId(),
                savedWarehouse
                        .getItems()
                        .get(0)
                        .getItemId()
        );
    }

    @Test
    void shouldFindWarehouseByName() {

        // Arrange
        Warehouse warehouse =
                new Warehouse("Secondary Warehouse");

        Warehouse savedWarehouse =
                warehouseRepository.save(warehouse);

        // Act
        Warehouse result =
                warehouseRepository
                        .findByName(
                                savedWarehouse.getName()
                        )
                        .orElseThrow();

        // Assert
        assertEquals(
                savedWarehouse.getId(),
                result.getId()
        );

        assertEquals(
                "Secondary Warehouse",
                result.getName().getValue()
        );
    }

    @Test
    void shouldRemoveItemFromWarehouse() {

        // Arrange
        Item item = new Item(
                "Monitor",
                "24 inch monitor",
                ItemStatus.NEW,
                ItemType.ELECTRONICS,
                new ItemHealth(8),
                LocalDate.now()
        );

        Item savedItem =
                itemRepository.save(item);

        Warehouse warehouse =
                new Warehouse("Storage");

        warehouse.addItem(savedItem);

        Warehouse savedWarehouse =
                warehouseRepository.save(warehouse);

        // Act
        savedWarehouse.removeItem(
                savedItem.getItemId()
        );

        Warehouse updated =
                warehouseRepository.save(
                        savedWarehouse
                );

        // Assert
        assertNotNull(updated);

        assertTrue(
                updated.getItems().isEmpty()
        );
    }
}