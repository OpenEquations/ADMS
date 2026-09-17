package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class DeleteTenderUseCase {

    private final TenderRepository tenderRepository;

    public DeleteTenderUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void execute(Long tenderId) {

        TenderId id = new TenderId(tenderId);

        if (!tenderRepository.existsById(id)) {
            throw new IllegalArgumentException("Tender not found");
        }

        tenderRepository.deleteById(id);
    }
}