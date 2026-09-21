package rw.adms.domain.items;

import java.time.LocalDateTime;

/**
 * An immutable point-in-time health reading for an item. Every health
 * change appends one of these rather than overwriting anything, so the
 * full decline (or recovery) trend stays visible - including the very
 * first reading taken when the item was created.
 */
public class ItemHealthRecord {

    private final Long id;
    private final Long itemId;
    private final int health;
    private final LocalDateTime recordedAt;

    public ItemHealthRecord(Long itemId, int health, LocalDateTime recordedAt) {
        this(null, itemId, health, recordedAt);
    }

    public ItemHealthRecord(Long id, Long itemId, int health, LocalDateTime recordedAt) {
        this.id = id;
        this.itemId = itemId;
        this.health = health;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public int getHealth() {
        return health;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
