package rw.adms.presentation.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
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
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.presentation.items.dto.ChangeItemStatusRequest;
import rw.adms.presentation.items.dto.ChangeItemTypeRequest;
import rw.adms.presentation.items.dto.CreateItemRequest;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
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

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /** Pulled in by every @WebMvcTest via the app's global argument resolver. */
    @MockitoBean
    private ValidateSessionUseCase validateSessionUseCase;

    @MockitoBean
    private CreateItemUseCase createItemUseCase;

    @MockitoBean
    private GetItemUseCase getItemUseCase;

    @MockitoBean
    private GetItemsUseCase getItemsUseCase;

    @MockitoBean
    private ChangeItemNameUseCase changeItemNameUseCase;

    @MockitoBean
    private ChangeItemDescriptionUseCase changeItemDescriptionUseCase;

    @MockitoBean
    private ChangeItemStatusUseCase changeItemStatusUseCase;

    @MockitoBean
    private ChangeItemTypeUseCase changeItemTypeUseCase;

    @MockitoBean
    private ChangeItemHealthUseCase changeItemHealthUseCase;

    @MockitoBean
    private GetItemHealthHistoryUseCase getItemHealthHistoryUseCase;

    @MockitoBean
    private DeleteItemUseCase deleteItemUseCase;

    private Item sampleItem() {
        return Item.reconstitute(
                1L,
                "Old Printer",
                "An old office printer",
                ItemStatus.IN_USE,
                ItemType.ELECTRONICS,
                8,
                LocalDate.of(2020, 1, 1),
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void shouldCreateItem() throws Exception {

        CreateItemRequest request = new CreateItemRequest(
                "Old Printer",
                "An old office printer",
                ItemStatus.IN_USE,
                ItemType.ELECTRONICS,
                8,
                LocalDate.of(2020, 1, 1)
        );

        when(createItemUseCase.execute(
                "Old Printer",
                "An old office printer",
                ItemStatus.IN_USE,
                ItemType.ELECTRONICS,
                new ItemHealth(8),
                LocalDate.of(2020, 1, 1)
        )).thenReturn(sampleItem());

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Old Printer"));

        verify(createItemUseCase).execute(
                "Old Printer",
                "An old office printer",
                ItemStatus.IN_USE,
                ItemType.ELECTRONICS,
                new ItemHealth(8),
                LocalDate.of(2020, 1, 1)
        );
    }

    @Test
    void shouldRejectCreateItemWithInvalidHealth() throws Exception {

        CreateItemRequest request = new CreateItemRequest(
                "Old Printer",
                "An old office printer",
                ItemStatus.IN_USE,
                ItemType.ELECTRONICS,
                142,
                LocalDate.of(2020, 1, 1)
        );

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnItemById() throws Exception {

        when(getItemUseCase.execute(1L)).thenReturn(sampleItem());

        mockMvc.perform(get("/api/items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemName").value("Old Printer"))
                .andExpect(jsonPath("$.itemHealth").value(8));
    }

    @Test
    void shouldReturn404WhenItemNotFound() throws Exception {

        when(getItemUseCase.execute(99L))
                .thenThrow(new IllegalArgumentException("Item not found"));

        mockMvc.perform(get("/api/items/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllItems() throws Exception {

        when(getItemsUseCase.execute()).thenReturn(List.of(sampleItem()));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Old Printer"));
    }

    @Test
    void shouldChangeItemStatus() throws Exception {

        ChangeItemStatusRequest request = new ChangeItemStatusRequest(ItemStatus.SOLD);

        mockMvc.perform(patch("/api/items/{id}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(changeItemStatusUseCase).execute(1L, ItemStatus.SOLD);
    }

    @Test
    void shouldChangeItemType() throws Exception {

        ChangeItemTypeRequest request = new ChangeItemTypeRequest(ItemType.FURNITURE);

        mockMvc.perform(patch("/api/items/{id}/type", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(changeItemTypeUseCase).execute(1L, ItemType.FURNITURE);
    }

    @Test
    void shouldReturnHealthHistory() throws Exception {

        rw.adms.domain.items.ItemHealthRecord record = new rw.adms.domain.items.ItemHealthRecord(
                1L, 1L, 55, java.time.LocalDateTime.of(2026, 1, 1, 0, 0)
        );

        when(getItemHealthHistoryUseCase.execute(1L)).thenReturn(java.util.List.of(record));

        mockMvc.perform(get("/api/items/{id}/health-history", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].health").value(55));
    }

    @Test
    void shouldDeleteItem() throws Exception {

        mockMvc.perform(delete("/api/items/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteItemUseCase).execute(1L);
    }

    @Test
    void shouldReturn404WhenDeletingMissingItem() throws Exception {

        doThrow(new IllegalArgumentException("Item not found"))
                .when(deleteItemUseCase).execute(99L);

        mockMvc.perform(delete("/api/items/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
