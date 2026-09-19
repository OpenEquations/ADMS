package rw.adms.presentation.company;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rw.adms.application.company.usecases.ChangeCompanyEmailUseCase;
import rw.adms.application.company.usecases.ChangeCompanyNameUseCase;
import rw.adms.application.company.usecases.CreateCompanyUseCase;
import rw.adms.application.company.usecases.DeleteCompanyUseCase;
import rw.adms.application.company.usecases.GetCompaniesUseCase;
import rw.adms.application.company.usecases.GetCompanyUseCase;
import rw.adms.domain.companies.Company;
import rw.adms.presentation.company.dto.ChangeCompanyEmailRequest;
import rw.adms.presentation.company.dto.CreateCompanyRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCompanyUseCase createCompanyUseCase;

    @MockitoBean
    private GetCompanyUseCase getCompanyUseCase;

    @MockitoBean
    private GetCompaniesUseCase getCompaniesUseCase;

    @MockitoBean
    private ChangeCompanyNameUseCase changeCompanyNameUseCase;

    @MockitoBean
    private ChangeCompanyEmailUseCase changeCompanyEmailUseCase;

    @MockitoBean
    private DeleteCompanyUseCase deleteCompanyUseCase;

    @Test
    void shouldCreateCompany() throws Exception {

        CreateCompanyRequest request = new CreateCompanyRequest("ACME Rwanda", "contact@acme.rw");

        Company createdCompany = Company.reconstitute(1L, "ACME Rwanda", "contact@acme.rw");

        when(createCompanyUseCase.execute("ACME Rwanda", "contact@acme.rw")).thenReturn(createdCompany);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ACME Rwanda"));

        verify(createCompanyUseCase).execute("ACME Rwanda", "contact@acme.rw");
    }

    @Test
    void shouldRejectCreateCompanyWithBlankName() throws Exception {

        CreateCompanyRequest request = new CreateCompanyRequest("", "contact@acme.rw");

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCompanyById() throws Exception {

        Company company = Company.reconstitute(1L, "ACME Rwanda", "contact@acme.rw");

        when(getCompanyUseCase.execute(1L)).thenReturn(company);

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ACME Rwanda"));
    }

    @Test
    void shouldReturn404WhenCompanyNotFound() throws Exception {

        when(getCompanyUseCase.execute(99L))
                .thenThrow(new IllegalArgumentException("Company not found"));

        mockMvc.perform(get("/api/companies/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllCompanies() throws Exception {

        Company company = Company.reconstitute(1L, "ACME Rwanda", "contact@acme.rw");

        when(getCompaniesUseCase.execute()).thenReturn(List.of(company));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ACME Rwanda"));
    }

    @Test
    void shouldChangeCompanyEmail() throws Exception {

        ChangeCompanyEmailRequest request = new ChangeCompanyEmailRequest("new@acme.rw");

        mockMvc.perform(patch("/api/companies/{id}/email", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(changeCompanyEmailUseCase).execute(1L, "new@acme.rw");
    }

    @Test
    void shouldDeleteCompany() throws Exception {

        mockMvc.perform(delete("/api/companies/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteCompanyUseCase).execute(1L);
    }

    @Test
    void shouldReturn404WhenDeletingMissingCompany() throws Exception {

        doThrow(new IllegalArgumentException("Company not found"))
                .when(deleteCompanyUseCase).execute(99L);

        mockMvc.perform(delete("/api/companies/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
