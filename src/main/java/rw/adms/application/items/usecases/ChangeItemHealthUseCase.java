package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.items.vo.ItemId;

public class ChangeItemHealthUseCase {

    private final ItemRepository itemRepository;

    public ChangeItemHealthUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void execute(Long itemId, Integer health) {

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        item.updateItemHealth(health);

        itemRepository.save(item);
    }
}