package rw.adms.presentation.tenders.dto;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.presentation.company.dto.CompanyResponse;
import rw.adms.presentation.items.dto.ItemResponse;

import java.util.List;

public record TenderResponse(
        Long id,
        String title,
        String description,
        List<ItemResponse> items,
        TenderType type,
        CompanyResponse tenderWinner,
        TenderStatus status
) {

    public static TenderResponse from(Tender tender) {
        return new TenderResponse(
                tender.getId().getValue(),
                tender.getTitle().getValue(),
                tender.getDescription(),
                tender.getItems().stream().map(ItemResponse::from).toList(),
                tender.getType(),
                tender.getTenderWinner() == null ? null : CompanyResponse.from(tender.getTenderWinner()),
                tender.getStatus()
        );
    }
}
