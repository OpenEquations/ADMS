package rw.adms.presentation.tenders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.presentation.tenders.dto.AddItemToTenderRequest;
import rw.adms.presentation.tenders.dto.ConcludeTenderRequest;
import rw.adms.presentation.tenders.dto.CreateTenderRequest;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TenderController.class)
class TenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTenderUseCase createTenderUseCase;

    @MockitoBean
    private GetTenderUseCase getTenderUseCase;

    @MockitoBean
    private GetTendersUseCase getTendersUseCase;

    @MockitoBean
    private GetTenderItemsUseCase getTenderItemsUseCase;

    @MockitoBean
    private ChangeTenderTitleUseCase changeTenderTitleUseCase;

    @MockitoBean
    private ChangeTenderDescriptionUseCase changeTenderDescriptionUseCase;

    @MockitoBean
    private ChangeTenderStatusUseCase changeTenderStatusUseCase;

    @MockitoBean
    private AddItemToTenderUseCase addItemToTenderUseCase;

    @MockitoBean
    private SetTenderWinnerUseCase setTenderWinnerUseCase;

    @MockitoBean
    private ConcludeTenderUseCase concludeTenderUseCase;

    @MockitoBean
    private DeleteTenderUseCase deleteTenderUseCase;

    private Tender sampleTender() {
        return Tender.reconstitute(
                1L,
                "Office equipment tender",
                "Selling old office equipment",
                List.of(),
                TenderType.SELLING_TENDER,
                null,
                TenderStatus.PUBLISHED
        );
    }

    @Test
    void shouldCreateTender() throws Exception {

        CreateTenderRequest request = new CreateTenderRequest(
                "Office equipment tender",
                "Selling old office equipment",
                TenderType.SELLING_TENDER
        );

        mockMvc.perform(post("/api/tenders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(createTenderUseCase).execute(
                "Office equipment tender",
                "Selling old office equipment",
                TenderType.SELLING_TENDER
        );
    }

    @Test
    void shouldRejectCreateTenderWithBlankTitle() throws Exception {

        CreateTenderRequest request = new CreateTenderRequest(
                "", "Selling old office equipment", TenderType.SELLING_TENDER
        );

        mockMvc.perform(post("/api/tenders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnTenderById() throws Exception {

        when(getTenderUseCase.execute(1L)).thenReturn(sampleTender());

        mockMvc.perform(get("/api/tenders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Office equipment tender"))
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    void shouldReturn404WhenTenderNotFound() throws Exception {

        when(getTenderUseCase.execute(99L))
                .thenThrow(new IllegalArgumentException("Tender not found"));

        mockMvc.perform(get("/api/tenders/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllTenders() throws Exception {

        when(getTendersUseCase.execute()).thenReturn(List.of(sampleTender()));

        mockMvc.perform(get("/api/tenders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Office equipment tender"));
    }

    @Test
    void shouldReturnTenderItems() throws Exception {

        Item item = Item.reconstitute(
                1L, "Old Printer", "Desc", ItemStatus.NO_LONGER_IN_USE, 3,
                LocalDate.now(), null, null, null, null, null
        );

        when(getTenderItemsUseCase.execute(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/api/tenders/{id}/items", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Old Printer"));
    }

    @Test
    void shouldAddItemToTender() throws Exception {

        AddItemToTenderRequest request = new AddItemToTenderRequest(5L);

        mockMvc.perform(post("/api/tenders/{id}/items", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(addItemToTenderUseCase).execute(1L, 5L);
    }

    @Test
    void shouldConcludeTender() throws Exception {

        ConcludeTenderRequest request = new ConcludeTenderRequest(7L);

        mockMvc.perform(post("/api/tenders/{id}/conclude", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(concludeTenderUseCase).execute(1L, 7L);
    }

    @Test
    void shouldDeleteTender() throws Exception {

        mockMvc.perform(delete("/api/tenders/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteTenderUseCase).execute(1L);
    }

    @Test
    void shouldReturn404WhenDeletingMissingTender() throws Exception {

        doThrow(new IllegalArgumentException("Tender not found"))
                .when(deleteTenderUseCase).execute(99L);

        mockMvc.perform(delete("/api/tenders/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
