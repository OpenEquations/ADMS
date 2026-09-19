package rw.adms.application.tenders.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

import java.util.List;

public class GetTenderItemsUseCase {

    private final TenderRepository tenderRepository;

    public GetTenderItemsUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public List<Item> execute(Long tenderId) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        return tender.getItems();
    }
}
