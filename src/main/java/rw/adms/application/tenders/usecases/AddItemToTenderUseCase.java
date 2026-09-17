package rw.adms.application.tenders.usecases;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class AddItemToTenderUseCase {

    private final TenderRepository tenderRepository;
    private final ItemRepository itemRepository;

    public AddItemToTenderUseCase(
            TenderRepository tenderRepository,
            ItemRepository itemRepository
    ) {
        this.tenderRepository = tenderRepository;
        this.itemRepository = itemRepository;
    }

    public void execute(Long tenderId, Long itemId) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        Item item = itemRepository.findById(
                new ItemId(itemId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Item not found"));

        tender.addItem(item);

        tenderRepository.save(tender);
    }
}