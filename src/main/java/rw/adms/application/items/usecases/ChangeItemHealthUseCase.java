package rw.adms.application.items.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.ItemHealthRecord;
import rw.adms.domain.items.interfaces.ItemHealthRecordRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

import java.time.LocalDateTime;

public class ChangeItemHealthUseCase {

    private final ItemRepository itemRepository;
    private final ItemHealthRecordRepository itemHealthRecordRepository;

    public ChangeItemHealthUseCase(
            ItemRepository itemRepository,
            ItemHealthRecordRepository itemHealthRecordRepository
    ) {
        this.itemRepository = itemRepository;
        this.itemHealthRecordRepository = itemHealthRecordRepository;
    }

    public void execute(Long itemId, Integer health) {

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        item.updateItemHealth(health);

        itemRepository.save(item);

        // Appended, never overwritten - this is what lets the trend show
        // the full decline instead of just the latest reading.
        itemHealthRecordRepository.save(new ItemHealthRecord(
                itemId,
                health,
                LocalDateTime.now()
        ));
    }
}
