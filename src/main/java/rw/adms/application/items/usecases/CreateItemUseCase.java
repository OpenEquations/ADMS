package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.ItemHealthRecord;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemHealthRecordRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateItemUseCase {

    private final ItemRepository itemRepository;
    private final ItemHealthRecordRepository itemHealthRecordRepository;

    public CreateItemUseCase(
            ItemRepository itemRepository,
            ItemHealthRecordRepository itemHealthRecordRepository
    ) {
        this.itemRepository = itemRepository;
        this.itemHealthRecordRepository = itemHealthRecordRepository;
    }

    public Item execute(
            String itemName,
            String itemDescription,
            ItemStatus itemStatus,
            ItemType itemType,
            ItemHealth itemHealth,
            LocalDate dateBought
    ) {

        Item item = new Item(
                itemName,
                itemDescription,
                itemStatus,
                itemType,
                itemHealth,
                dateBought
        );

        Item saved = itemRepository.save(item);

        // The first reading in the trend - without this, a health change
        // made right after creation would look like the item's history
        // started at the new value, losing what it was bought at.
        itemHealthRecordRepository.save(new ItemHealthRecord(
                saved.getItemId().getValue(),
                saved.getItemHealth().getValue(),
                LocalDateTime.now()
        ));

        return saved;
    }
}
