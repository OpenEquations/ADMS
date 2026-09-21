package rw.adms.application.items.usecases;

import rw.adms.domain.items.ItemHealthRecord;
import rw.adms.domain.items.interfaces.ItemHealthRecordRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

import java.util.List;

public class GetItemHealthHistoryUseCase {

    private final ItemRepository itemRepository;
    private final ItemHealthRecordRepository itemHealthRecordRepository;

    public GetItemHealthHistoryUseCase(
            ItemRepository itemRepository,
            ItemHealthRecordRepository itemHealthRecordRepository
    ) {
        this.itemRepository = itemRepository;
        this.itemHealthRecordRepository = itemHealthRecordRepository;
    }

    public List<ItemHealthRecord> execute(Long itemId) {

        if (!itemRepository.existsById(new ItemId(itemId))) {
            throw new IllegalArgumentException("Item not found");
        }

        return itemHealthRecordRepository.findByItemId(itemId);
    }
}
