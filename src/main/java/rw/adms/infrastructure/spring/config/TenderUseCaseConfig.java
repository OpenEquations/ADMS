package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.tenders.usecases.AddItemToTenderUseCase;
import rw.adms.application.tenders.usecases.ChangeTenderDeadlineUseCase;
import rw.adms.application.tenders.usecases.ChangeTenderDescriptionUseCase;
import rw.adms.application.tenders.usecases.ChangeTenderStatusUseCase;
import rw.adms.application.tenders.usecases.ChangeTenderTitleUseCase;
import rw.adms.application.tenders.usecases.ConcludeTenderUseCase;
import rw.adms.application.tenders.usecases.CreateTenderUseCase;
import rw.adms.application.tenders.usecases.DeleteTenderUseCase;
import rw.adms.application.tenders.usecases.GetTenderItemsUseCase;
import rw.adms.application.tenders.usecases.GetTenderUseCase;
import rw.adms.application.tenders.usecases.GetTendersUseCase;
import rw.adms.application.tenders.usecases.SetTenderWinnerUseCase;
import rw.adms.domain.companies.interfaces.CompanyRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.tenders.interfaces.TenderRepository;

/**
 * Wires the tenders bounded context's use cases as Spring beans.
 */
@Configuration
public class TenderUseCaseConfig {

    @Bean
    public CreateTenderUseCase createTenderUseCase(TenderRepository tenderRepository) {
        return new CreateTenderUseCase(tenderRepository);
    }

    @Bean
    public GetTenderUseCase getTenderUseCase(TenderRepository tenderRepository) {
        return new GetTenderUseCase(tenderRepository);
    }

    @Bean
    public GetTendersUseCase getTendersUseCase(TenderRepository tenderRepository) {
        return new GetTendersUseCase(tenderRepository);
    }

    @Bean
    public GetTenderItemsUseCase getTenderItemsUseCase(TenderRepository tenderRepository) {
        return new GetTenderItemsUseCase(tenderRepository);
    }

    @Bean
    public ChangeTenderTitleUseCase changeTenderTitleUseCase(TenderRepository tenderRepository) {
        return new ChangeTenderTitleUseCase(tenderRepository);
    }

    @Bean
    public ChangeTenderDescriptionUseCase changeTenderDescriptionUseCase(TenderRepository tenderRepository) {
        return new ChangeTenderDescriptionUseCase(tenderRepository);
    }

    @Bean
    public ChangeTenderStatusUseCase changeTenderStatusUseCase(TenderRepository tenderRepository) {
        return new ChangeTenderStatusUseCase(tenderRepository);
    }

    @Bean
    public ChangeTenderDeadlineUseCase changeTenderDeadlineUseCase(TenderRepository tenderRepository) {
        return new ChangeTenderDeadlineUseCase(tenderRepository);
    }

    @Bean
    public AddItemToTenderUseCase addItemToTenderUseCase(
            TenderRepository tenderRepository,
            ItemRepository itemRepository
    ) {
        return new AddItemToTenderUseCase(tenderRepository, itemRepository);
    }

    @Bean
    public SetTenderWinnerUseCase setTenderWinnerUseCase(
            TenderRepository tenderRepository,
            CompanyRepository companyRepository
    ) {
        return new SetTenderWinnerUseCase(tenderRepository, companyRepository);
    }

    @Bean
    public ConcludeTenderUseCase concludeTenderUseCase(
            TenderRepository tenderRepository,
            CompanyRepository companyRepository
    ) {
        return new ConcludeTenderUseCase(tenderRepository, companyRepository);
    }

    @Bean
    public DeleteTenderUseCase deleteTenderUseCase(TenderRepository tenderRepository) {
        return new DeleteTenderUseCase(tenderRepository);
    }
}
