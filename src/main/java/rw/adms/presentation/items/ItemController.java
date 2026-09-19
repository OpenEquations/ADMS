package rw.adms.presentation.items;

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
import rw.adms.application.items.usecases.ChangeItemDescriptionUseCase;
import rw.adms.application.items.usecases.ChangeItemHealthUseCase;
import rw.adms.application.items.usecases.ChangeItemNameUseCase;
import rw.adms.application.items.usecases.ChangeItemStatusUseCase;
import rw.adms.application.items.usecases.CreateItemUseCase;
import rw.adms.application.items.usecases.DeleteItemUseCase;
import rw.adms.application.items.usecases.GetItemUseCase;
import rw.adms.application.items.usecases.GetItemsUseCase;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.presentation.items.dto.ChangeItemDescriptionRequest;
import rw.adms.presentation.items.dto.ChangeItemHealthRequest;
import rw.adms.presentation.items.dto.ChangeItemNameRequest;
import rw.adms.presentation.items.dto.ChangeItemStatusRequest;
import rw.adms.presentation.items.dto.CreateItemRequest;
import rw.adms.presentation.items.dto.ItemResponse;

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
    private final ChangeItemHealthUseCase changeItemHealthUseCase;
    private final DeleteItemUseCase deleteItemUseCase;

    public ItemController(
            CreateItemUseCase createItemUseCase,
            GetItemUseCase getItemUseCase,
            GetItemsUseCase getItemsUseCase,
            ChangeItemNameUseCase changeItemNameUseCase,
            ChangeItemDescriptionUseCase changeItemDescriptionUseCase,
            ChangeItemStatusUseCase changeItemStatusUseCase,
            ChangeItemHealthUseCase changeItemHealthUseCase,
            DeleteItemUseCase deleteItemUseCase
    ) {
        this.createItemUseCase = createItemUseCase;
        this.getItemUseCase = getItemUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.changeItemNameUseCase = changeItemNameUseCase;
        this.changeItemDescriptionUseCase = changeItemDescriptionUseCase;
        this.changeItemStatusUseCase = changeItemStatusUseCase;
        this.changeItemHealthUseCase = changeItemHealthUseCase;
        this.deleteItemUseCase = deleteItemUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateItemRequest request) {

        createItemUseCase.execute(
                request.itemName(),
                request.itemDescription(),
                request.itemStatus(),
                new ItemHealth(request.itemHealth()),
                request.dateBought()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<ItemResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ItemResponse.from(getItemUseCase.execute(id))
        );
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeItemNameRequest request
    ) {
        changeItemNameUseCase.execute(id, request.itemName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<Void> changeDescription(
            @PathVariable Long id,
            @Valid @RequestBody ChangeItemDescriptionRequest request
    ) {
        changeItemDescriptionUseCase.execute(id, request.itemDescription());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeItemStatusRequest request
    ) {
        changeItemStatusUseCase.execute(id, request.itemStatus());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/health")
    public ResponseEntity<Void> changeHealth(
            @PathVariable Long id,
            @Valid @RequestBody ChangeItemHealthRequest request
    ) {
        changeItemHealthUseCase.execute(id, request.itemHealth());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
