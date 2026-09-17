package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

public class GetItemUseCase {

    private final ItemRepository itemRepository;

    public GetItemUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item execute(Long itemId) {

        return itemRepository.findById(new ItemId(itemId))
                .orElseThrow(() ->
                        new IllegalArgumentException("Item not found"));
    }
}