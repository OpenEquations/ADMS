package rw.adms.domain.items.interfaces;

import rw.adms.domain.items.ItemHealthRecord;

import java.util.List;

public interface ItemHealthRecordRepository {

    ItemHealthRecord save(ItemHealthRecord record);

    /**
     * Oldest first, so callers can plot/print the trend in chronological
     * order without re-sorting.
     */
    List<ItemHealthRecord> findByItemId(Long itemId);

    void deleteByItemId(Long itemId);
}
