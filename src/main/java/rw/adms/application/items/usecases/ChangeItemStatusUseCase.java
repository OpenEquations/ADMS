package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

public class ChangeItemStatusUseCase {

    private final ItemRepository itemRepository;

    public ChangeItemStatusUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void execute(Long itemId, ItemStatus newStatus) {

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        item.updateItemStatus(newStatus);

        itemRepository.save(item);
    }
}