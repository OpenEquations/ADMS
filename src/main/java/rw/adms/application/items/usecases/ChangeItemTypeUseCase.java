package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

public class ChangeItemTypeUseCase {

    private final ItemRepository itemRepository;

    public ChangeItemTypeUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void execute(Long itemId, ItemType newType) {

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        item.updateItemType(newType);

        itemRepository.save(item);
    }
}
