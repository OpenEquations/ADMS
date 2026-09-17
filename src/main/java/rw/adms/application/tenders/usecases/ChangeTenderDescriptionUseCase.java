package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class ChangeTenderDescriptionUseCase {

    private final TenderRepository tenderRepository;

    public ChangeTenderDescriptionUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void execute(Long tenderId, String newDescription) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        tender.setDescription(newDescription);

        tenderRepository.save(tender);
    }
}