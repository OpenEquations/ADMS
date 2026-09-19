package rw.adms.infrastructure.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.adms.application.warehouses.usecases.AddItemToWarehouseUseCase;
import rw.adms.application.warehouses.usecases.ChangeWarehouseNameUseCase;
import rw.adms.application.warehouses.usecases.CreateWarehouseUseCase;
import rw.adms.application.warehouses.usecases.DeleteWarehouseUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseItemUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseItemsUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseUseCase;
import rw.adms.application.warehouses.usecases.GetWarehousesUseCase;
import rw.adms.application.warehouses.usecases.RemoveItemFromWarehouseUseCase;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

/**
 * Wires the warehouses bounded context's use cases as Spring beans.
 */
@Configuration
public class WarehouseUseCaseConfig {

    @Bean
    public CreateWarehouseUseCase createWarehouseUseCase(WarehouseRepository warehouseRepository) {
        return new CreateWarehouseUseCase(warehouseRepository);
    }

    @Bean
    public GetWarehouseUseCase getWarehouseUseCase(WarehouseRepository warehouseRepository) {
        return new GetWarehouseUseCase(warehouseRepository);
    }

    @Bean
    public GetWarehousesUseCase getWarehousesUseCase(WarehouseRepository warehouseRepository) {
        return new GetWarehousesUseCase(warehouseRepository);
    }

    @Bean
    public GetWarehouseItemUseCase getWarehouseItemUseCase(WarehouseRepository warehouseRepository) {
        return new GetWarehouseItemUseCase(warehouseRepository);
    }

    @Bean
    public GetWarehouseItemsUseCase getWarehouseItemsUseCase(WarehouseRepository warehouseRepository) {
        return new GetWarehouseItemsUseCase(warehouseRepository);
    }

    @Bean
    public ChangeWarehouseNameUseCase changeWarehouseNameUseCase(WarehouseRepository warehouseRepository) {
        return new ChangeWarehouseNameUseCase(warehouseRepository);
    }

    @Bean
    public AddItemToWarehouseUseCase addItemToWarehouseUseCase(
            WarehouseRepository warehouseRepository,
            ItemRepository itemRepository
    ) {
        return new AddItemToWarehouseUseCase(warehouseRepository, itemRepository);
    }

    @Bean
    public RemoveItemFromWarehouseUseCase removeItemFromWarehouseUseCase(WarehouseRepository warehouseRepository) {
        return new RemoveItemFromWarehouseUseCase(warehouseRepository);
    }

    @Bean
    public DeleteWarehouseUseCase deleteWarehouseUseCase(WarehouseRepository warehouseRepository) {
        return new DeleteWarehouseUseCase(warehouseRepository);
    }
}
