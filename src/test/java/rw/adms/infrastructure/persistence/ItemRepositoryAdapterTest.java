package rw.adms.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.shared.vo.Money;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryAdapterTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldSaveAndRetrieveItem() {

        // Arrange
        Item item = new Item(
                "Laptop",
                "Dell Latitude laptop",
                ItemStatus.NEW,
                new ItemHealth(9),
                LocalDate.now()
        );

        // Act
        Item savedItem =
                itemRepository.save(item);

        // Assert
        assertNotNull(savedItem);
        assertNotNull(savedItem.getItemId());

        assertEquals(
                "Laptop",
                savedItem.getItemName()
        );

        assertEquals(
                "Dell Latitude laptop",
                savedItem.getItemDescription()
        );

        assertEquals(
                ItemStatus.NEW,
                savedItem.getItemStatus()
        );

        assertEquals(
                9,
                savedItem.getItemHealth().getValue()
        );
    }

    @Test
    void shouldUpdateItemName() {

        // Arrange
        Item item = new Item(
                "Old Laptop",
                "Laptop description",
                ItemStatus.NEW,
                new ItemHealth(8),
                LocalDate.now()
        );

        Item savedItem =
                itemRepository.save(item);

        // Act
        savedItem.editItemName(
                "New Laptop"
        );

        Item updated =
                itemRepository.save(savedItem);

        // Assert
        assertEquals(
                savedItem.getItemId(),
                updated.getItemId()
        );

        assertEquals(
                "New Laptop",
                updated.getItemName()
        );
    }

    @Test
    void shouldPersistMoney() {

        // Arrange
        Item item = new Item(
                "Printer",
                "Office printer",
                ItemStatus.NEW,
                new ItemHealth(7),
                LocalDate.now()
        );

        Item savedItem =
                itemRepository.save(item);

        // Act
        savedItem.setRepairCost(
                new Money(
                        new BigDecimal("15000.00"),
                        "RWF"
                )
        );

        Item updated =
                itemRepository.save(savedItem);

        // Assert
        assertNotNull(
                updated.getRepairCost()
        );

        assertEquals(
                new BigDecimal("15000.00"),
                updated.getRepairCost().getAmount()
        );

        assertEquals(
                "RWF",
                updated.getRepairCost().getCurrency()
        );
    }
}