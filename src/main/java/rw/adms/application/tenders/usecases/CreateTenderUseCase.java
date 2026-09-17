package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.interfaces.TenderRepository;


public class CreateTenderUseCase {

    private final TenderRepository tenderRepository;

    public CreateTenderUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void execute(
            String title,
            String description,
            TenderType type
    ) {

        Tender tender = new Tender(
                title,
                description,
                type
        );

        tenderRepository.save(tender);
    }
}