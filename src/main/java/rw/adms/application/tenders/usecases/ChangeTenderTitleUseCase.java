package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;
import rw.adms.domain.tenders.vo.TenderTitle;

public class ChangeTenderTitleUseCase {

    private final TenderRepository tenderRepository;

    public ChangeTenderTitleUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void execute(Long tenderId, String newTitle) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        tender.changeTitle(new TenderTitle(newTitle));

        tenderRepository.save(tender);
    }
}