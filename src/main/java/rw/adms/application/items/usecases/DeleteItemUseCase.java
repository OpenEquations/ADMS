package rw.adms.application.items.usecases;

import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

public class DeleteItemUseCase {

    private final ItemRepository itemRepository;

    public DeleteItemUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void execute(Long itemId) {

        ItemId id = new ItemId(itemId);

        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Item not found");
        }

        itemRepository.deleteById(id);
    }
}
