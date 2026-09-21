package rw.adms.infrastructure.persistence.entities;

import jakarta.persistence.*;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tenders")
public class TenderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenderType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_company_id")
    private CompanyJpaEntity tenderWinner;

    private LocalDateTime deadline;

    @ManyToMany
    @JoinTable(
            name = "tender_items",
            joinColumns = @JoinColumn(name = "tender_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_tender_item",
                            columnNames = {
                                    "tender_id",
                                    "item_id"
                            }
                    )
            }
    )
    private List<ItemJpaEntity> items = new ArrayList<>();

    protected TenderJpaEntity() {
    }

    public TenderJpaEntity(
            String title,
            String description,
            TenderType type,
            TenderStatus status,
            LocalDateTime deadline
    ) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TenderType getType() {
        return type;
    }

    public TenderStatus getStatus() {
        return status;
    }

    public CompanyJpaEntity getTenderWinner() {
        return tenderWinner;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public List<ItemJpaEntity> getItems() {
        return items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TenderType type) {
        this.type = type;
    }

    public void setStatus(TenderStatus status) {
        this.status = status;
    }

    public void setTenderWinner(CompanyJpaEntity tenderWinner) {
        this.tenderWinner = tenderWinner;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setItems(List<ItemJpaEntity> items) {
        this.items = items;
    }
}