package rw.adms.domain.tenders;

import rw.adms.domain.items.Item;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.vo.TenderId;
import rw.adms.domain.tenders.vo.TenderTitle;
import rw.adms.domain.company.Company;

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

    public Tender(
            String title,
            String description,
            TenderType type
    ) {
        this.title = new TenderTitle(title);
        this.description = description;
        this.type = type;
        this.status = TenderStatus.NOT_PUBLISHED;
    }

    // =========================
    // Getters
    // =========================

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

    // =========================
    // Setters / Behaviors
    // =========================

    public void setDescription(String description) {
        this.description = description;
    }

    public void changeTenderTitle(String title) {
        this.title = new TenderTitle(title);
    }

    public void changeTenderStatus(TenderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Tender status cannot be null"
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