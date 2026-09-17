package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;

import java.time.LocalDate;

public class CreateItemUseCase {

    private final ItemRepository itemRepository;

    public CreateItemUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void execute(
            String itemName,
            String itemDescription,
            ItemStatus itemStatus,
            ItemHealth itemHealth,
            LocalDate dateBought
    ) {

        Item item = new Item(
                itemName,
                itemDescription,
                itemStatus,
                itemHealth,
                dateBought
        );

        itemRepository.save(item);
    }
}