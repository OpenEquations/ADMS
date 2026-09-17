package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;

import java.util.List;

public class GetItemsUseCase {

    private final ItemRepository itemRepository;

    public GetItemsUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> execute() {
        return itemRepository.findAll();
    }
}