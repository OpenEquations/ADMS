package rw.adms.presentation.reports;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
import rw.adms.application.reports.usecases.GenerateStockReportUseCase;
import rw.adms.application.reports.usecases.GetStockReportUseCase;
import rw.adms.application.reports.usecases.GetStockReportsUseCase;
import rw.adms.domain.reports.StockReport;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockReportController.class)
class StockReportControllerTest {

    private static final String TOKEN = "test-token";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenerateStockReportUseCase generateStockReportUseCase;

    @MockitoBean
    private GetStockReportsUseCase getStockReportsUseCase;

    @MockitoBean
    private GetStockReportUseCase getStockReportUseCase;

    /** Pulled in by every @WebMvcTest via the app's global argument resolver. */
    @MockitoBean
    private ValidateSessionUseCase validateSessionUseCase;

    private StockReport sampleReport() {
        return new StockReport(
                1L,
                LocalDateTime.of(2026, 1, 1, 9, 0),
                "Joe LeBonheur",
                10, 2, 3, 1, 4,
                List.of(new StockReport.ItemCount("NEW", 5)),
                List.of(new StockReport.ItemCount("ELECTRONICS", 6)),
                List.of(new StockReport.WarehouseItemCount("Main Warehouse", 6)),
                List.of(new StockReport.LowHealthItem(9L, "Old Printer", 12, "IN_USE"))
        );
    }

    private MockHttpServletRequestBuilder authenticated(MockHttpServletRequestBuilder builder) {
        User actor = User.reconstitute(
                1L, "Joe", "LeBonheur", "joe@example.com", "hash", UserRole.SUPERADMIN, Set.of()
        );
        when(validateSessionUseCase.execute(TOKEN)).thenReturn(Optional.of(actor));
        return builder.header("Authorization", "Bearer " + TOKEN);
    }

    @Test
    void shouldGenerateReportWhenAuthenticated() throws Exception {

        when(generateStockReportUseCase.execute("Joe LeBonheur")).thenReturn(sampleReport());

        mockMvc.perform(authenticated(post("/api/reports/stock")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalItems").value(10))
                .andExpect(jsonPath("$.generatedByName").value("Joe LeBonheur"));
    }

    @Test
    void shouldRejectGenerateWithoutAuthentication() throws Exception {

        mockMvc.perform(post("/api/reports/stock"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldListReports() throws Exception {

        when(getStockReportsUseCase.execute()).thenReturn(List.of(sampleReport()));

        mockMvc.perform(get("/api/reports/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalItems").value(10));
    }

    @Test
    void shouldReturnReportById() throws Exception {

        when(getStockReportUseCase.execute(1L)).thenReturn(sampleReport());

        mockMvc.perform(get("/api/reports/stock/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lowHealthItems[0].itemName").value("Old Printer"));
    }

    @Test
    void shouldReturn404WhenReportNotFound() throws Exception {

        when(getStockReportUseCase.execute(99L))
                .thenThrow(new IllegalArgumentException("Report not found"));

        mockMvc.perform(get("/api/reports/stock/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
