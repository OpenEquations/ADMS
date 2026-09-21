package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.interfaces.TenderRepository;

import java.time.LocalDateTime;


public class CreateTenderUseCase {

    private final TenderRepository tenderRepository;

    public CreateTenderUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public Tender execute(
            String title,
            String description,
            TenderType type,
            LocalDateTime deadline
    ) {

        Tender tender = new Tender(
                title,
                description,
                type,
                deadline
        );

        return tenderRepository.save(tender);
    }
}