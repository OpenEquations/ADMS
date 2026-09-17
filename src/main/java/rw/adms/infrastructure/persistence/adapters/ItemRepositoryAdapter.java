package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.vo.CompanyId;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.shared.vo.Money;
import rw.adms.infrastructure.persistence.entities.CompanyJpaEntity;
import rw.adms.infrastructure.persistence.entities.ItemJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataCompanyRepository;
import rw.adms.infrastructure.persistence.repositories.SpringDataItemRepository;

import java.util.List;
import java.util.Optional;

@Repository
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
    public void save(Item item) {

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
                            "Item not found"
                    )
            );

            entity.setItemName(item.getItemName());
            entity.setItemDescription(item.getItemDescription());
            entity.setItemStatus(item.getItemStatus());
            entity.setItemHealth(item.getItemHealth().getValue());
            entity.setDateBought(item.getDateBought());
            entity.setUpdatedAt(item.getUpdatedAt());
        }

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

        if (item.getSoldTo() != null) {

            CompanyId companyId =
                    item.getSoldTo().getId();

            if (companyId == null) {
                throw new IllegalArgumentException(
                        "Sold company must already exist"
                );
            }

            CompanyJpaEntity company =
                    companyRepository.findById(
                            companyId.getValue()
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Sold company not found"
                            )
                    );

            entity.setSoldTo(company);

        } else {
            entity.setSoldTo(null);
        }

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

        itemRepository.save(entity);
    }

    @Override
    public Optional<Item> findById(ItemId id) {

        return itemRepository.findById(id.getValue())
                .map(this::toDomain);
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

        return itemRepository.existsById(id.getValue());
    }

    @Override
    public void deleteById(ItemId id) {

        itemRepository.deleteById(id.getValue());
    }

    private Item toDomain(ItemJpaEntity entity) {

        Money soldAt = null;

        if (entity.getSoldAtAmount() != null) {
            soldAt = new Money(
                    entity.getSoldAtAmount(),
                    entity.getSoldAtCurrency()
            );
        }

        Company soldTo = null;

        if (entity.getSoldTo() != null) {
            soldTo = Company.reconstitute(
                    entity.getSoldTo().getId(),
                    entity.getSoldTo().getName(),
                    entity.getSoldTo().getEmail()
            );
        }

        Money repairCost = null;

        if (entity.getRepairCostAmount() != null) {
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