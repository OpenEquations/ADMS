package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.TenderStatus;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class ChangeTenderStatusUseCase {

    private final TenderRepository tenderRepository;

    public ChangeTenderStatusUseCase(
            TenderRepository tenderRepository
    ) {
        this.tenderRepository = tenderRepository;
    }

    public void execute(
            Long tenderId,
            TenderStatus newStatus
    ) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        tender.changeTenderStatus(newStatus);

        tenderRepository.save(tender);
    }
}