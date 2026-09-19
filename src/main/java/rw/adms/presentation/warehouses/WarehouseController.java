package rw.adms.presentation.warehouses;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.warehouses.usecases.AddItemToWarehouseUseCase;
import rw.adms.application.warehouses.usecases.ChangeWarehouseNameUseCase;
import rw.adms.application.warehouses.usecases.CreateWarehouseUseCase;
import rw.adms.application.warehouses.usecases.DeleteWarehouseUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseItemUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseItemsUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseUseCase;
import rw.adms.application.warehouses.usecases.GetWarehousesUseCase;
import rw.adms.application.warehouses.usecases.RemoveItemFromWarehouseUseCase;
import rw.adms.presentation.items.dto.ItemResponse;
import rw.adms.presentation.warehouses.dto.AddItemToWarehouseRequest;
import rw.adms.presentation.warehouses.dto.ChangeWarehouseNameRequest;
import rw.adms.presentation.warehouses.dto.CreateWarehouseRequest;
import rw.adms.presentation.warehouses.dto.WarehouseResponse;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final CreateWarehouseUseCase createWarehouseUseCase;
    private final GetWarehouseUseCase getWarehouseUseCase;
    private final GetWarehousesUseCase getWarehousesUseCase;
    private final GetWarehouseItemUseCase getWarehouseItemUseCase;
    private final GetWarehouseItemsUseCase getWarehouseItemsUseCase;
    private final ChangeWarehouseNameUseCase changeWarehouseNameUseCase;
    private final AddItemToWarehouseUseCase addItemToWarehouseUseCase;
    private final RemoveItemFromWarehouseUseCase removeItemFromWarehouseUseCase;
    private final DeleteWarehouseUseCase deleteWarehouseUseCase;

    public WarehouseController(
            CreateWarehouseUseCase createWarehouseUseCase,
            GetWarehouseUseCase getWarehouseUseCase,
            GetWarehousesUseCase getWarehousesUseCase,
            GetWarehouseItemUseCase getWarehouseItemUseCase,
            GetWarehouseItemsUseCase getWarehouseItemsUseCase,
            ChangeWarehouseNameUseCase changeWarehouseNameUseCase,
            AddItemToWarehouseUseCase addItemToWarehouseUseCase,
            RemoveItemFromWarehouseUseCase removeItemFromWarehouseUseCase,
            DeleteWarehouseUseCase deleteWarehouseUseCase
    ) {
        this.createWarehouseUseCase = createWarehouseUseCase;
        this.getWarehouseUseCase = getWarehouseUseCase;
        this.getWarehousesUseCase = getWarehousesUseCase;
        this.getWarehouseItemUseCase = getWarehouseItemUseCase;
        this.getWarehouseItemsUseCase = getWarehouseItemsUseCase;
        this.changeWarehouseNameUseCase = changeWarehouseNameUseCase;
        this.addItemToWarehouseUseCase = addItemToWarehouseUseCase;
        this.removeItemFromWarehouseUseCase = removeItemFromWarehouseUseCase;
        this.deleteWarehouseUseCase = deleteWarehouseUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateWarehouseRequest request) {

        createWarehouseUseCase.execute(request.name());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAll() {

        List<WarehouseResponse> warehouses = getWarehousesUseCase.execute()
                .stream()
                .map(WarehouseResponse::from)
                .toList();

        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                WarehouseResponse.from(getWarehouseUseCase.execute(id))
        );
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemResponse>> getItems(@PathVariable Long id) {

        List<ItemResponse> items = getWarehouseItemsUseCase.execute(id)
                .stream()
                .map(ItemResponse::from)
                .toList();

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}/items/{itemId}")
    public ResponseEntity<ItemResponse> getItem(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                ItemResponse.from(getWarehouseItemUseCase.execute(id, itemId))
        );
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable Long id,
            @Valid @RequestBody AddItemToWarehouseRequest request
    ) {
        addItemToWarehouseUseCase.execute(id, request.itemId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        removeItemFromWarehouseUseCase.execute(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeWarehouseNameRequest request
    ) {
        changeWarehouseNameUseCase.execute(id, request.name());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteWarehouseUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
