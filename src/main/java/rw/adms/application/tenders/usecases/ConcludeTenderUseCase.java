package rw.adms.application.tenders.usecases;

import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.companies.Company;
import rw.adms.domain.companies.vo.CompanyId;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

public class ConcludeTenderUseCase {

    private final TenderRepository tenderRepository;
    private final CompanyRepository companyRepository;

    public ConcludeTenderUseCase(
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

        tender.changeTenderStatus(TenderStatus.OVER);

        tenderRepository.save(tender);
    }
}