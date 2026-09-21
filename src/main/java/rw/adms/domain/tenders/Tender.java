package rw.adms.domain.tenders;

import rw.adms.domain.companies.Company;
import rw.adms.domain.items.Item;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.vo.TenderId;
import rw.adms.domain.tenders.vo.TenderTitle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tender {

    private TenderId id;
    private TenderTitle title;
    private String description;

    private final List<Item> items = new ArrayList<>();

    private TenderType type;
    private Company tenderWinner;
    private TenderStatus status;
    private LocalDateTime deadline;

    public Tender(
            String title,
            String description,
            TenderType type,
            LocalDateTime deadline
    ) {
        this.title = new TenderTitle(title);
        this.description = description;
        this.type = type;
        this.status = TenderStatus.NOT_PUBLISHED;
        this.deadline = deadline;
    }

    private Tender(
            TenderId id,
            TenderTitle title,
            String description,
            List<Item> items,
            TenderType type,
            Company tenderWinner,
            TenderStatus status,
            LocalDateTime deadline
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.items.addAll(items);
        this.type = type;
        this.tenderWinner = tenderWinner;
        this.status = status;
        this.deadline = deadline;
    }

    public static Tender reconstitute(
            Long id,
            String title,
            String description,
            List<Item> items,
            TenderType type,
            Company tenderWinner,
            TenderStatus status,
            LocalDateTime deadline
    ) {
        return new Tender(
                new TenderId(id),
                new TenderTitle(title),
                description,
                items,
                type,
                tenderWinner,
                status,
                deadline
        );
    }

    public TenderId getId() {
        return id;
    }

    public TenderTitle getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public TenderType getType() {
        return type;
    }

    public Company getTenderWinner() {
        return tenderWinner;
    }

    public TenderStatus getStatus() {
        return status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void changeTenderTitle(String title) {
        this.title = new TenderTitle(title);
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void changeTenderStatus(TenderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Tender status cannot be null"
            );
        }

        if (status == TenderStatus.PUBLISHED && deadline == null) {
            throw new IllegalArgumentException(
                    "Tender cannot be published without a deadline"
            );
        }

        this.status = status;
    }

    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Item cannot be null"
            );
        }

        if (items.stream()
                .anyMatch(existingItem ->
                        existingItem.getItemId().equals(item.getItemId()))) {

            throw new IllegalArgumentException(
                    "Item already exists on this tender"
            );
        }

        items.add(item);
    }

    public void setTenderWinner(Company company) {
        this.tenderWinner = company;
    }
}