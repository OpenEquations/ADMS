package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.companies.Company;
import rw.adms.domain.items.Item;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;
import rw.adms.infrastructure.persistence.entities.CompanyJpaEntity;
import rw.adms.infrastructure.persistence.entities.ItemJpaEntity;
import rw.adms.infrastructure.persistence.entities.TenderJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataCompanyRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataItemRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataTenderRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class TenderRepositoryAdapter
        implements TenderRepository {

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
    public void save(Tender tender) {

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
                            "Tender not found"
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

        List<ItemJpaEntity> items =
                tender.getItems()
                        .stream()
                        .map(item -> itemRepository.findById(
                                item.getItemId().getValue()
                        ).orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Item not found: "
                                                + item.getItemId()
                                )
                        ))
                        .toList();

        entity.setItems(items);

        if (tender.getTenderWinner() != null) {

            if (tender.getTenderWinner().getId() == null) {
                throw new IllegalArgumentException(
                        "Tender winner must already exist"
                );
            }

            CompanyJpaEntity company =
                    companyRepository.findById(
                            tender.getTenderWinner()
                                    .getId()
                                    .getValue()
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Tender winner company not found"
                            )
                    );

            entity.setTenderWinner(company);

        } else {
            entity.setTenderWinner(null);
        }

        tenderRepository.save(entity);
    }

    @Override
    public Optional<Tender> findById(TenderId id) {

        return tenderRepository.findById(id.getValue())
                .map(this::toDomain);
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

        tenderRepository.deleteById(id.getValue());
    }

    private Tender toDomain(TenderJpaEntity entity) {

        List<Item> items =
                entity.getItems()
                        .stream()
                        .map(this::toItem)
                        .toList();

        Company winner = null;

        if (entity.getTenderWinner() != null) {

            CompanyJpaEntity company =
                    entity.getTenderWinner();

            winner = Company.reconstitute(
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
                winner,
                entity.getStatus()
        );
    }

    private Item toItem(ItemJpaEntity entity) {

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