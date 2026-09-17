package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

public class ChangeItemDescriptionUseCase {

    private final ItemRepository itemRepository;

    public ChangeItemDescriptionUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void execute(Long itemId, String newDescription) {

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        item.updateItemDescription(newDescription);

        itemRepository.save(item);
    }
}