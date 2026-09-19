package rw.adms.presentation.items.dto;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.presentation.common.MoneyResponse;
import rw.adms.presentation.company.dto.CompanyResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String itemName,
        String itemDescription,
        ItemStatus itemStatus,
        int itemHealth,
        LocalDate dateBought,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        MoneyResponse soldAt,
        CompanyResponse soldTo,
        MoneyResponse repairCost
) {

    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getItemId().getValue(),
                item.getItemName(),
                item.getItemDescription(),
                item.getItemStatus(),
                item.getItemHealth().getValue(),
                item.getDateBought(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                MoneyResponse.from(item.getSoldAt()),
                item.getSoldTo() == null ? null : CompanyResponse.from(item.getSoldTo()),
                MoneyResponse.from(item.getRepairCost())
        );
    }
}
