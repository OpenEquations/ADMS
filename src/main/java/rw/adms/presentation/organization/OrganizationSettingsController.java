package rw.adms.presentation.organization;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.adms.application.organization.usecases.GetOrganizationSettingsUseCase;
import rw.adms.application.organization.usecases.UpdateOrganizationSettingsUseCase;
import rw.adms.domain.organization.OrganizationSettings;
import rw.adms.presentation.organization.dto.OrganizationSettingsRequest;
import rw.adms.presentation.organization.dto.OrganizationSettingsResponse;

/**
 * A single settings resource, not a collection - there is exactly one
 * organization profile per deployment, so GET never 404s (it returns an
 * empty-fields response until the profile has been set) and PUT always
 * upserts the one row.
 */
@RestController
@RequestMapping("/api/organization")
public class OrganizationSettingsController {

    private final GetOrganizationSettingsUseCase getOrganizationSettingsUseCase;
    private final UpdateOrganizationSettingsUseCase updateOrganizationSettingsUseCase;

    public OrganizationSettingsController(
            GetOrganizationSettingsUseCase getOrganizationSettingsUseCase,
            UpdateOrganizationSettingsUseCase updateOrganizationSettingsUseCase
    ) {
        this.getOrganizationSettingsUseCase = getOrganizationSettingsUseCase;
        this.updateOrganizationSettingsUseCase = updateOrganizationSettingsUseCase;
    }

    @GetMapping
    public ResponseEntity<OrganizationSettingsResponse> get() {

        return ResponseEntity.ok(
                getOrganizationSettingsUseCase.execute()
                        .map(OrganizationSettingsResponse::from)
                        .orElseGet(OrganizationSettingsResponse::empty)
        );
    }

    @PutMapping
    public ResponseEntity<OrganizationSettingsResponse> update(
            @Valid @RequestBody OrganizationSettingsRequest request
    ) {
        OrganizationSettings settings = updateOrganizationSettingsUseCase.execute(
                request.name(),
                request.email(),
                request.phone(),
                request.address(),
                request.website(),
                request.registrationNumber(),
                request.description()
        );

        return ResponseEntity.ok(OrganizationSettingsResponse.from(settings));
    }
}
