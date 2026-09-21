package rw.adms.presentation.items;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.items.usecases.ChangeItemDescriptionUseCase;
import rw.adms.application.items.usecases.ChangeItemHealthUseCase;
import rw.adms.application.items.usecases.ChangeItemNameUseCase;
import rw.adms.application.items.usecases.ChangeItemStatusUseCase;
import rw.adms.application.items.usecases.ChangeItemTypeUseCase;
import rw.adms.application.items.usecases.CreateItemUseCase;
import rw.adms.application.items.usecases.DeleteItemUseCase;
import rw.adms.application.items.usecases.GetItemHealthHistoryUseCase;
import rw.adms.application.items.usecases.GetItemUseCase;
import rw.adms.application.items.usecases.GetItemsUseCase;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.presentation.items.dto.ChangeItemDescriptionRequest;
import rw.adms.presentation.items.dto.ChangeItemHealthRequest;
import rw.adms.presentation.items.dto.ChangeItemNameRequest;
import rw.adms.presentation.items.dto.ChangeItemStatusRequest;
import rw.adms.presentation.items.dto.ChangeItemTypeRequest;
import rw.adms.presentation.items.dto.CreateItemRequest;
import rw.adms.presentation.items.dto.ItemHealthRecordResponse;
import rw.adms.presentation.items.dto.ItemResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final GetItemUseCase getItemUseCase;
    private final GetItemsUseCase getItemsUseCase;
    private final ChangeItemNameUseCase changeItemNameUseCase;
    private final ChangeItemDescriptionUseCase changeItemDescriptionUseCase;
    private final ChangeItemStatusUseCase changeItemStatusUseCase;
    private final ChangeItemTypeUseCase changeItemTypeUseCase;
    private final ChangeItemHealthUseCase changeItemHealthUseCase;
    private final GetItemHealthHistoryUseCase getItemHealthHistoryUseCase;
    private final DeleteItemUseCase deleteItemUseCase;

    public ItemController(
            CreateItemUseCase createItemUseCase,
            GetItemUseCase getItemUseCase,
            GetItemsUseCase getItemsUseCase,
            ChangeItemNameUseCase changeItemNameUseCase,
            ChangeItemDescriptionUseCase changeItemDescriptionUseCase,
            ChangeItemStatusUseCase changeItemStatusUseCase,
            ChangeItemTypeUseCase changeItemTypeUseCase,
            ChangeItemHealthUseCase changeItemHealthUseCase,
            GetItemHealthHistoryUseCase getItemHealthHistoryUseCase,
            DeleteItemUseCase deleteItemUseCase
    ) {
        this.createItemUseCase = createItemUseCase;
        this.getItemUseCase = getItemUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.changeItemNameUseCase = changeItemNameUseCase;
        this.changeItemDescriptionUseCase = changeItemDescriptionUseCase;
        this.changeItemStatusUseCase = changeItemStatusUseCase;
        this.changeItemTypeUseCase = changeItemTypeUseCase;
        this.changeItemHealthUseCase = changeItemHealthUseCase;
        this.getItemHealthHistoryUseCase = getItemHealthHistoryUseCase;
        this.deleteItemUseCase = deleteItemUseCase;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody CreateItemRequest request) {

        Item item = createItemUseCase.execute(
                request.itemName(),
                request.itemDescription(),
                request.itemStatus(),
                request.itemType(),
                new ItemHealth(request.itemHealth()),
                request.dateBought()
        );

        return ResponseEntity
                .created(URI.create("/api/items/" + item.getItemId().getValue()))
                .body(ItemResponse.from(item));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAll() {

        List<ItemResponse> items = getItemsUseCase.execute()
                .stream()
                .map(ItemResponse::from)
                .toList();

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(
                ItemResponse.from(getItemUseCase.execute(id))
        );
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> changeName(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeItemNameRequest request
    ) {
        changeItemNameUseCase.execute(id, request.itemName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<Void> changeDescription(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeItemDescriptionRequest request
    ) {
        changeItemDescriptionUseCase.execute(id, request.itemDescription());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeItemStatusRequest request
    ) {
        changeItemStatusUseCase.execute(id, request.itemStatus());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/type")
    public ResponseEntity<Void> changeType(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeItemTypeRequest request
    ) {
        changeItemTypeUseCase.execute(id, request.itemType());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/health")
    public ResponseEntity<Void> changeHealth(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeItemHealthRequest request
    ) {
        changeItemHealthUseCase.execute(id, request.itemHealth());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/health-history")
    public ResponseEntity<List<ItemHealthRecordResponse>> getHealthHistory(@PathVariable("id") Long id) {

        List<ItemHealthRecordResponse> history = getItemHealthHistoryUseCase.execute(id)
                .stream()
                .map(ItemHealthRecordResponse::from)
                .toList();

        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        deleteItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
