package rw.adms.presentation.organization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
import rw.adms.application.organization.usecases.GetOrganizationSettingsUseCase;
import rw.adms.application.organization.usecases.UpdateOrganizationSettingsUseCase;
import rw.adms.domain.organization.OrganizationSettings;
import rw.adms.presentation.organization.dto.OrganizationSettingsRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizationSettingsController.class)
class OrganizationSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /** Pulled in by every @WebMvcTest via the app's global argument resolver. */
    @MockitoBean
    private ValidateSessionUseCase validateSessionUseCase;

    @MockitoBean
    private GetOrganizationSettingsUseCase getOrganizationSettingsUseCase;

    @MockitoBean
    private UpdateOrganizationSettingsUseCase updateOrganizationSettingsUseCase;

    @Test
    void shouldReturnEmptyResponseWhenNotConfigured() throws Exception {

        when(getOrganizationSettingsUseCase.execute()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/organization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").doesNotExist());
    }

    @Test
    void shouldReturnSettingsWhenConfigured() throws Exception {

        OrganizationSettings settings = new OrganizationSettings(
                "ADMS Rwanda",
                "contact@adms.rw",
                "+250700000000",
                "KG 7 Ave, Kigali",
                "https://adms.rw",
                "RDB-12345",
                "Asset disposal authority"
        );

        when(getOrganizationSettingsUseCase.execute()).thenReturn(Optional.of(settings));

        mockMvc.perform(get("/api/organization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMS Rwanda"))
                .andExpect(jsonPath("$.email").value("contact@adms.rw"));
    }

    @Test
    void shouldUpdateSettings() throws Exception {

        OrganizationSettingsRequest request = new OrganizationSettingsRequest(
                "ADMS Rwanda",
                "contact@adms.rw",
                "+250700000000",
                "KG 7 Ave, Kigali",
                "https://adms.rw",
                "RDB-12345",
                "Asset disposal authority"
        );

        OrganizationSettings saved = new OrganizationSettings(
                "ADMS Rwanda",
                "contact@adms.rw",
                "+250700000000",
                "KG 7 Ave, Kigali",
                "https://adms.rw",
                "RDB-12345",
                "Asset disposal authority"
        );

        when(updateOrganizationSettingsUseCase.execute(
                "ADMS Rwanda",
                "contact@adms.rw",
                "+250700000000",
                "KG 7 Ave, Kigali",
                "https://adms.rw",
                "RDB-12345",
                "Asset disposal authority"
        )).thenReturn(saved);

        mockMvc.perform(put("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMS Rwanda"));

        verify(updateOrganizationSettingsUseCase).execute(
                "ADMS Rwanda",
                "contact@adms.rw",
                "+250700000000",
                "KG 7 Ave, Kigali",
                "https://adms.rw",
                "RDB-12345",
                "Asset disposal authority"
        );
    }

    @Test
    void shouldRejectUpdateWithBlankName() throws Exception {

        OrganizationSettingsRequest request = new OrganizationSettingsRequest(
                "", null, null, null, null, null, null
        );

        mockMvc.perform(put("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
