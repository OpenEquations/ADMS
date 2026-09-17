package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class GetTenderUseCase {

    private final TenderRepository tenderRepository;

    public GetTenderUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public Tender execute(Long tenderId) {

        return tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));
    }
}