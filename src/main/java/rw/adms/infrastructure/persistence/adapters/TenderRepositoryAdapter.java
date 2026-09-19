package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rw.adms.domain.companies.Company;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;
import rw.adms.infrastructure.persistence.entities.CompanyJpaEntity;
import rw.adms.infrastructure.persistence.entities.ItemJpaEntity;
import rw.adms.infrastructure.persistence.entities.TenderJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataCompanyRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataItemRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataTenderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class-level {@code @Transactional}: Tender carries lazy {@code items} and
 * {@code tenderWinner} associations, so the session needs to stay open
 * across the read AND the entity-to-domain mapping below.
 */
@Repository
@Transactional
public class TenderRepositoryAdapter implements TenderRepository {

    private final SpringDataTenderRepository tenderRepository;
    private final SpringDataItemRepository itemRepository;
    private final SpringDataCompanyRepository companyRepository;

    public TenderRepositoryAdapter(
            SpringDataTenderRepository tenderRepository,
            SpringDataItemRepository itemRepository,
            SpringDataCompanyRepository companyRepository
    ) {
        this.tenderRepository = tenderRepository;
        this.itemRepository = itemRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Tender save(Tender tender) {

        TenderJpaEntity entity;

        if (tender.getId() == null) {

            entity = new TenderJpaEntity(
                    tender.getTitle().getValue(),
                    tender.getDescription(),
                    tender.getType(),
                    tender.getStatus()
            );

        } else {

            entity = tenderRepository.findById(
                    tender.getId().getValue()
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "Tender not found: "
                                    + tender.getId()
                    )
            );

            entity.setTitle(
                    tender.getTitle().getValue()
            );

            entity.setDescription(
                    tender.getDescription()
            );

            entity.setType(
                    tender.getType()
            );

            entity.setStatus(
                    tender.getStatus()
            );
        }

        // -------------------------
        // Items
        // -------------------------

        // Mutable list: Hibernate's merge reconciles a many-to-many
        // collection in place (clear() + repopulate), so an immutable
        // List (e.g. from Stream.toList()) would blow up here.
        List<ItemJpaEntity> itemEntities =
                tender.getItems()
                        .stream()
                        .map(item ->
                                itemRepository.findById(
                                        item.getItemId().getValue()
                                ).orElseThrow(() ->
                                        new IllegalArgumentException(
                                                "Item not found: "
                                                        + item.getItemId()
                                        )
                                )
                        )
                        .collect(Collectors.toCollection(ArrayList::new));

        entity.setItems(itemEntities);

        // -------------------------
        // Tender winner
        // -------------------------

        if (tender.getTenderWinner() != null) {

            Company company =
                    tender.getTenderWinner();

            CompanyJpaEntity companyEntity =
                    companyRepository.findById(
                            company.getId().getValue()
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Company not found: "
                                            + company.getId()
                            )
                    );

            entity.setTenderWinner(
                    companyEntity
            );

        } else {

            entity.setTenderWinner(null);
        }

        // -------------------------
        // Save
        // -------------------------

        TenderJpaEntity savedEntity =
                tenderRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Optional<Tender> findById(TenderId id) {

        return tenderRepository.findById(
                id.getValue()
        ).map(this::toDomain);
    }

    @Override
    public List<Tender> findAll() {

        return tenderRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(TenderId id) {

        return tenderRepository.existsById(
                id.getValue()
        );
    }

    @Override
    public void deleteById(TenderId id) {

        tenderRepository.deleteById(
                id.getValue()
        );
    }

    private Tender toDomain(
            TenderJpaEntity entity
    ) {

        List<Item> items =
                entity.getItems()
                        .stream()
                        .map(this::itemToDomain)
                        .toList();

        Company tenderWinner = null;

        if (entity.getTenderWinner() != null) {

            CompanyJpaEntity company =
                    entity.getTenderWinner();

            tenderWinner = Company.reconstitute(
                    company.getId(),
                    company.getName(),
                    company.getEmail()
            );
        }

        return Tender.reconstitute(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                items,
                entity.getType(),
                tenderWinner,
                entity.getStatus()
        );
    }

    private Item itemToDomain(
            ItemJpaEntity entity
    ) {

        return Item.reconstitute(
                entity.getId(),
                entity.getItemName(),
                entity.getItemDescription(),
                entity.getItemStatus(),
                entity.getItemHealth(),
                entity.getDateBought(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                null,
                null,
                null
        );
    }
}