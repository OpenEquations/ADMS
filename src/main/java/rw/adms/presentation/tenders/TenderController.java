package rw.adms.presentation.tenders;

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
import rw.adms.application.tenders.usecases.AddItemToTenderUseCase;
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
import rw.adms.presentation.items.dto.ItemResponse;
import rw.adms.presentation.tenders.dto.AddItemToTenderRequest;
import rw.adms.presentation.tenders.dto.ChangeTenderDescriptionRequest;
import rw.adms.presentation.tenders.dto.ChangeTenderStatusRequest;
import rw.adms.presentation.tenders.dto.ChangeTenderTitleRequest;
import rw.adms.presentation.tenders.dto.ConcludeTenderRequest;
import rw.adms.presentation.tenders.dto.CreateTenderRequest;
import rw.adms.presentation.tenders.dto.SetTenderWinnerRequest;
import rw.adms.presentation.tenders.dto.TenderResponse;
import rw.adms.domain.tenders.Tender;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {

    private final CreateTenderUseCase createTenderUseCase;
    private final GetTenderUseCase getTenderUseCase;
    private final GetTendersUseCase getTendersUseCase;
    private final GetTenderItemsUseCase getTenderItemsUseCase;
    private final ChangeTenderTitleUseCase changeTenderTitleUseCase;
    private final ChangeTenderDescriptionUseCase changeTenderDescriptionUseCase;
    private final ChangeTenderStatusUseCase changeTenderStatusUseCase;
    private final AddItemToTenderUseCase addItemToTenderUseCase;
    private final SetTenderWinnerUseCase setTenderWinnerUseCase;
    private final ConcludeTenderUseCase concludeTenderUseCase;
    private final DeleteTenderUseCase deleteTenderUseCase;

    public TenderController(
            CreateTenderUseCase createTenderUseCase,
            GetTenderUseCase getTenderUseCase,
            GetTendersUseCase getTendersUseCase,
            GetTenderItemsUseCase getTenderItemsUseCase,
            ChangeTenderTitleUseCase changeTenderTitleUseCase,
            ChangeTenderDescriptionUseCase changeTenderDescriptionUseCase,
            ChangeTenderStatusUseCase changeTenderStatusUseCase,
            AddItemToTenderUseCase addItemToTenderUseCase,
            SetTenderWinnerUseCase setTenderWinnerUseCase,
            ConcludeTenderUseCase concludeTenderUseCase,
            DeleteTenderUseCase deleteTenderUseCase
    ) {
        this.createTenderUseCase = createTenderUseCase;
        this.getTenderUseCase = getTenderUseCase;
        this.getTendersUseCase = getTendersUseCase;
        this.getTenderItemsUseCase = getTenderItemsUseCase;
        this.changeTenderTitleUseCase = changeTenderTitleUseCase;
        this.changeTenderDescriptionUseCase = changeTenderDescriptionUseCase;
        this.changeTenderStatusUseCase = changeTenderStatusUseCase;
        this.addItemToTenderUseCase = addItemToTenderUseCase;
        this.setTenderWinnerUseCase = setTenderWinnerUseCase;
        this.concludeTenderUseCase = concludeTenderUseCase;
        this.deleteTenderUseCase = deleteTenderUseCase;
    }

    @PostMapping
    public ResponseEntity<TenderResponse> create(@Valid @RequestBody CreateTenderRequest request) {

        Tender tender = createTenderUseCase.execute(
                request.title(),
                request.description(),
                request.type()
        );

        return ResponseEntity
                .created(URI.create("/api/tenders/" + tender.getId().getValue()))
                .body(TenderResponse.from(tender));
    }

    @GetMapping
    public ResponseEntity<List<TenderResponse>> getAll() {

        List<TenderResponse> tenders = getTendersUseCase.execute()
                .stream()
                .map(TenderResponse::from)
                .toList();

        return ResponseEntity.ok(tenders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenderResponse> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(
                TenderResponse.from(getTenderUseCase.execute(id))
        );
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemResponse>> getItems(@PathVariable("id") Long id) {

        List<ItemResponse> items = getTenderItemsUseCase.execute(id)
                .stream()
                .map(ItemResponse::from)
                .toList();

        return ResponseEntity.ok(items);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable("id") Long id,
            @Valid @RequestBody AddItemToTenderRequest request
    ) {
        addItemToTenderUseCase.execute(id, request.itemId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<Void> changeTitle(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeTenderTitleRequest request
    ) {
        changeTenderTitleUseCase.execute(id, request.title());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<Void> changeDescription(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeTenderDescriptionRequest request
    ) {
        changeTenderDescriptionUseCase.execute(id, request.description());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangeTenderStatusRequest request
    ) {
        changeTenderStatusUseCase.execute(id, request.status());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/winner")
    public ResponseEntity<Void> setWinner(
            @PathVariable("id") Long id,
            @Valid @RequestBody SetTenderWinnerRequest request
    ) {
        setTenderWinnerUseCase.execute(id, request.companyId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/conclude")
    public ResponseEntity<Void> conclude(
            @PathVariable("id") Long id,
            @Valid @RequestBody ConcludeTenderRequest request
    ) {
        concludeTenderUseCase.execute(id, request.companyId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        deleteTenderUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
