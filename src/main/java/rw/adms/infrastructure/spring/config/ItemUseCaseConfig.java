package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.items.usecases.ChangeItemDescriptionUseCase;
import rw.adms.application.items.usecases.ChangeItemHealthUseCase;
import rw.adms.application.items.usecases.ChangeItemNameUseCase;
import rw.adms.application.items.usecases.ChangeItemStatusUseCase;
import rw.adms.application.items.usecases.ChangeItemTypeUseCase;
import rw.adms.application.items.usecases.CreateItemUseCase;
import rw.adms.application.items.usecases.DeleteItemUseCase;
import rw.adms.application.items.usecases.GetItemUseCase;
import rw.adms.application.items.usecases.GetItemsUseCase;
import rw.adms.domain.items.interfaces.ItemRepository;

/**
 * Wires the items bounded context's use cases as Spring beans.
 */
@Configuration
public class ItemUseCaseConfig {

    @Bean
    public CreateItemUseCase createItemUseCase(ItemRepository itemRepository) {
        return new CreateItemUseCase(itemRepository);
    }

    @Bean
    public GetItemUseCase getItemUseCase(ItemRepository itemRepository) {
        return new GetItemUseCase(itemRepository);
    }

    @Bean
    public GetItemsUseCase getItemsUseCase(ItemRepository itemRepository) {
        return new GetItemsUseCase(itemRepository);
    }

    @Bean
    public ChangeItemNameUseCase changeItemNameUseCase(ItemRepository itemRepository) {
        return new ChangeItemNameUseCase(itemRepository);
    }

    @Bean
    public ChangeItemDescriptionUseCase changeItemDescriptionUseCase(ItemRepository itemRepository) {
        return new ChangeItemDescriptionUseCase(itemRepository);
    }

    @Bean
    public ChangeItemStatusUseCase changeItemStatusUseCase(ItemRepository itemRepository) {
        return new ChangeItemStatusUseCase(itemRepository);
    }

    @Bean
    public ChangeItemTypeUseCase changeItemTypeUseCase(ItemRepository itemRepository) {
        return new ChangeItemTypeUseCase(itemRepository);
    }

    @Bean
    public ChangeItemHealthUseCase changeItemHealthUseCase(ItemRepository itemRepository) {
        return new ChangeItemHealthUseCase(itemRepository);
    }

    @Bean
    public DeleteItemUseCase deleteItemUseCase(ItemRepository itemRepository) {
        return new DeleteItemUseCase(itemRepository);
    }
}
