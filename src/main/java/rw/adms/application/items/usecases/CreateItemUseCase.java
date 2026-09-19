package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;

import java.time.LocalDate;

public class CreateItemUseCase {

    private final ItemRepository itemRepository;

    public CreateItemUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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

        return itemRepository.save(item);
    }
}
