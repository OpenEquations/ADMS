package rw.adms.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.infrastructure.spring.AdmsApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AdmsApplication.class)
class TenderRepositoryAdapterTest {

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldSaveTenderWithItemsAndWinner() {

        // =========================
        // Company
        // =========================

        Company company = new Company(
                "Repair Company Ltd",
                "repair@example.com"
        );

        Company savedCompany =
                companyRepository.save(company);

        assertNotNull(savedCompany.getId());

        // =========================
        // Item
        // =========================

        Item item = new Item(
                "Broken Generator",
                "Generator requiring repair",
                ItemStatus.NO_LONGER_IN_USE,
                ItemType.OTHER,
                new ItemHealth(3),
                LocalDate.now()
        );

        Item savedItem =
                itemRepository.save(item);

        assertNotNull(savedItem.getItemId());

        // =========================
        // Tender
        // =========================

        Tender tender = new Tender(
                "Generator Repair Tender",
                "Repair old generator",
                TenderType.REPAIR_TENDER,
                LocalDateTime.now().plusDays(7)
        );

        tender.addItem(savedItem);

        tender.setTenderWinner(
                savedCompany
        );

        tender.changeTenderStatus(
                TenderStatus.PUBLISHED
        );

        // Act
        Tender savedTender =
                tenderRepository.save(tender);

        // =========================
        // Assertions
        // =========================

        assertNotNull(
                savedTender
        );

        assertNotNull(
                savedTender.getId()
        );

        assertEquals(
                "Generator Repair Tender",
                savedTender.getTitle().getValue()
        );

        assertEquals(
                TenderType.REPAIR_TENDER,
                savedTender.getType()
        );

        assertEquals(
                TenderStatus.PUBLISHED,
                savedTender.getStatus()
        );

        assertEquals(
                1,
                savedTender.getItems().size()
        );

        assertEquals(
                savedItem.getItemId(),
                savedTender
                        .getItems()
                        .get(0)
                        .getItemId()
        );

        assertNotNull(
                savedTender.getTenderWinner()
        );

        assertEquals(
                savedCompany.getId(),
                savedTender
                        .getTenderWinner()
                        .getId()
        );
    }
}