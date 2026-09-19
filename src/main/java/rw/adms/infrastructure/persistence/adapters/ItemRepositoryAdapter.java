package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rw.adms.domain.companies.Company;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.shared.vo.Money;
import rw.adms.infrastructure.persistence.entities.CompanyJpaEntity;
import rw.adms.infrastructure.persistence.entities.ItemJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataCompanyRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataItemRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Class-level {@code @Transactional}: Item carries a lazy {@code soldTo}
 * association, so the session needs to stay open across the read AND the
 * entity-to-domain mapping below.
 */
@Repository
@Transactional
public class ItemRepositoryAdapter implements ItemRepository {

    private final SpringDataItemRepository itemRepository;
    private final SpringDataCompanyRepository companyRepository;

    public ItemRepositoryAdapter(
            SpringDataItemRepository itemRepository,
            SpringDataCompanyRepository companyRepository
    ) {
        this.itemRepository = itemRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Item save(Item item) {

        ItemJpaEntity entity;

        if (item.getItemId() == null) {

            entity = new ItemJpaEntity(
                    item.getItemName(),
                    item.getItemDescription(),
                    item.getItemStatus(),
                    item.getItemHealth().getValue(),
                    item.getDateBought(),
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            );

        } else {

            entity = itemRepository.findById(
                    item.getItemId().getValue()
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "Item not found: " + item.getItemId()
                    )
            );

            entity.setItemName(
                    item.getItemName()
            );

            entity.setItemDescription(
                    item.getItemDescription()
            );

            entity.setItemStatus(
                    item.getItemStatus()
            );

            entity.setItemHealth(
                    item.getItemHealth().getValue()
            );

            entity.setDateBought(
                    item.getDateBought()
            );

            entity.setCreatedAt(
                    item.getCreatedAt()
            );

            entity.setUpdatedAt(
                    item.getUpdatedAt()
            );
        }

        // -------------------------
        // Sold price
        // -------------------------

        if (item.getSoldAt() != null) {

            entity.setSoldAtAmount(
                    item.getSoldAt().getAmount()
            );

            entity.setSoldAtCurrency(
                    item.getSoldAt().getCurrency()
            );

        } else {

            entity.setSoldAtAmount(null);
            entity.setSoldAtCurrency(null);
        }

        // -------------------------
        // Sold to company
        // -------------------------

        if (item.getSoldTo() != null) {

            Company company =
                    item.getSoldTo();

            CompanyJpaEntity companyEntity =
                    companyRepository.findById(
                            company.getId().getValue()
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Company not found: "
                                            + company.getId()
                            )
                    );

            entity.setSoldTo(companyEntity);

        } else {

            entity.setSoldTo(null);
        }

        // -------------------------
        // Repair cost
        // -------------------------

        if (item.getRepairCost() != null) {

            entity.setRepairCostAmount(
                    item.getRepairCost().getAmount()
            );

            entity.setRepairCostCurrency(
                    item.getRepairCost().getCurrency()
            );

        } else {

            entity.setRepairCostAmount(null);
            entity.setRepairCostCurrency(null);
        }

        // -------------------------
        // Save
        // -------------------------

        ItemJpaEntity savedEntity =
                itemRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Optional<Item> findById(ItemId id) {

        return itemRepository.findById(
                id.getValue()
        ).map(this::toDomain);
    }

    @Override
    public List<Item> findAll() {

        return itemRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(ItemId id) {

        return itemRepository.existsById(
                id.getValue()
        );
    }

    @Override
    public void deleteById(ItemId id) {

        itemRepository.deleteById(
                id.getValue()
        );
    }

    private Item toDomain(ItemJpaEntity entity) {

        Money soldAt = null;

        if (
                entity.getSoldAtAmount() != null
                        && entity.getSoldAtCurrency() != null
        ) {
            soldAt = new Money(
                    entity.getSoldAtAmount(),
                    entity.getSoldAtCurrency()
            );
        }

        Company soldTo = null;

        if (entity.getSoldTo() != null) {

            CompanyJpaEntity company =
                    entity.getSoldTo();

            soldTo = Company.reconstitute(
                    company.getId(),
                    company.getName(),
                    company.getEmail()
            );
        }

        Money repairCost = null;

        if (
                entity.getRepairCostAmount() != null
                        && entity.getRepairCostCurrency() != null
        ) {
            repairCost = new Money(
                    entity.getRepairCostAmount(),
                    entity.getRepairCostCurrency()
            );
        }

        return Item.reconstitute(
                entity.getId(),
                entity.getItemName(),
                entity.getItemDescription(),
                entity.getItemStatus(),
                entity.getItemHealth(),
                entity.getDateBought(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                soldAt,
                soldTo,
                repairCost
        );
    }
}