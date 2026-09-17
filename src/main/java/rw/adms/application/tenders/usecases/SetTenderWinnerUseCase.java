package rw.adms.application.tenders.usecases;

import rw.adms.domain.company.Company;
import rw.adms.domain.company.interfaces.CompanyRepository;
import rw.adms.domain.company.vo.CompanyId;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class SetTenderWinnerUseCase {

    private final TenderRepository tenderRepository;
    private final CompanyRepository companyRepository;

    public SetTenderWinnerUseCase(
            TenderRepository tenderRepository,
            CompanyRepository companyRepository
    ) {
        this.tenderRepository = tenderRepository;
        this.companyRepository = companyRepository;
    }

    public void execute(Long tenderId, Long companyId) {

        Tender tender = tenderRepository.findById(
                new TenderId(tenderId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Tender not found"));

        Company company = companyRepository.findById(
                new CompanyId(companyId)
        ).orElseThrow(() ->
                new IllegalArgumentException("Company not found"));

        tender.setTenderWinner(company);

        tenderRepository.save(tender);
    }
}