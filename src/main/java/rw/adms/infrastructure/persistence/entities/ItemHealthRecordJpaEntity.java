package rw.adms.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "item_health_records",
        indexes = {
                @Index(name = "idx_item_health_records_item_id", columnList = "itemId")
        }
)
public class ItemHealthRecordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Integer health;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    protected ItemHealthRecordJpaEntity() {
    }

    public ItemHealthRecordJpaEntity(Long itemId, Integer health, LocalDateTime recordedAt) {
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

    public Integer getHealth() {
        return health;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
