package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

import java.time.LocalDateTime;

public class ChangeTenderDeadlineUseCase {

    private final TenderRepository tenderRepository;

    public ChangeTenderDeadlineUseCase(
            TenderRepository tenderRepository
    ) {
        this.tenderRepository = tenderRepository;
    }

    public void execute(
            Long tenderId,
            LocalDateTime deadline
    ) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        tender.setDeadline(deadline);

        tenderRepository.save(tender);
    }
}
