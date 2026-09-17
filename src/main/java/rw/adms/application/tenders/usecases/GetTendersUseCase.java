package rw.adms.application.tenders.usecases;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;

import java.util.List;

public class GetTendersUseCase {

    private final TenderRepository tenderRepository;

    public GetTendersUseCase(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public List<Tender> execute() {
        return tenderRepository.findAll();
    }
}