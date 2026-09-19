package rw.adms.presentation.company;

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
import rw.adms.application.company.usecases.ChangeCompanyEmailUseCase;
import rw.adms.application.company.usecases.ChangeCompanyNameUseCase;
import rw.adms.application.company.usecases.CreateCompanyUseCase;
import rw.adms.application.company.usecases.DeleteCompanyUseCase;
import rw.adms.application.company.usecases.GetCompaniesUseCase;
import rw.adms.application.company.usecases.GetCompanyUseCase;
import rw.adms.presentation.company.dto.ChangeCompanyEmailRequest;
import rw.adms.presentation.company.dto.ChangeCompanyNameRequest;
import rw.adms.presentation.company.dto.CompanyResponse;
import rw.adms.presentation.company.dto.CreateCompanyRequest;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CreateCompanyUseCase createCompanyUseCase;
    private final GetCompanyUseCase getCompanyUseCase;
    private final GetCompaniesUseCase getCompaniesUseCase;
    private final ChangeCompanyNameUseCase changeCompanyNameUseCase;
    private final ChangeCompanyEmailUseCase changeCompanyEmailUseCase;
    private final DeleteCompanyUseCase deleteCompanyUseCase;

    public CompanyController(
            CreateCompanyUseCase createCompanyUseCase,
            GetCompanyUseCase getCompanyUseCase,
            GetCompaniesUseCase getCompaniesUseCase,
            ChangeCompanyNameUseCase changeCompanyNameUseCase,
            ChangeCompanyEmailUseCase changeCompanyEmailUseCase,
            DeleteCompanyUseCase deleteCompanyUseCase
    ) {
        this.createCompanyUseCase = createCompanyUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
        this.getCompaniesUseCase = getCompaniesUseCase;
        this.changeCompanyNameUseCase = changeCompanyNameUseCase;
        this.changeCompanyEmailUseCase = changeCompanyEmailUseCase;
        this.deleteCompanyUseCase = deleteCompanyUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateCompanyRequest request) {

        createCompanyUseCase.execute(request.name(), request.email());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAll() {

        List<CompanyResponse> companies = getCompaniesUseCase.execute()
                .stream()
                .map(CompanyResponse::from)
                .toList();

        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                CompanyResponse.from(getCompanyUseCase.execute(id))
        );
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> changeName(
            @PathVariable Long id,
            @Valid @RequestBody ChangeCompanyNameRequest request
    ) {
        changeCompanyNameUseCase.execute(id, request.name());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Void> changeEmail(
            @PathVariable Long id,
            @Valid @RequestBody ChangeCompanyEmailRequest request
    ) {
        changeCompanyEmailUseCase.execute(id, request.email());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteCompanyUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
