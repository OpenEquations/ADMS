package rw.adms.presentation.warehouses;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
import rw.adms.application.warehouses.usecases.AddItemToWarehouseUseCase;
import rw.adms.application.warehouses.usecases.ChangeWarehouseNameUseCase;
import rw.adms.application.warehouses.usecases.CreateWarehouseUseCase;
import rw.adms.application.warehouses.usecases.DeleteWarehouseUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseItemUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseItemsUseCase;
import rw.adms.application.warehouses.usecases.GetWarehouseUseCase;
import rw.adms.application.warehouses.usecases.GetWarehousesUseCase;
import rw.adms.application.warehouses.usecases.RemoveItemFromWarehouseUseCase;
import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.presentation.warehouses.dto.AddItemToWarehouseRequest;
import rw.adms.presentation.warehouses.dto.ChangeWarehouseNameRequest;
import rw.adms.presentation.warehouses.dto.CreateWarehouseRequest;
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

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /** Pulled in by every @WebMvcTest via the app's global argument resolver. */
    @MockitoBean
    private ValidateSessionUseCase validateSessionUseCase;

    @MockitoBean
    private CreateWarehouseUseCase createWarehouseUseCase;

    @MockitoBean
    private GetWarehouseUseCase getWarehouseUseCase;

    @MockitoBean
    private GetWarehousesUseCase getWarehousesUseCase;

    @MockitoBean
    private GetWarehouseItemUseCase getWarehouseItemUseCase;

    @MockitoBean
    private GetWarehouseItemsUseCase getWarehouseItemsUseCase;

    @MockitoBean
    private ChangeWarehouseNameUseCase changeWarehouseNameUseCase;

    @MockitoBean
    private AddItemToWarehouseUseCase addItemToWarehouseUseCase;

    @MockitoBean
    private RemoveItemFromWarehouseUseCase removeItemFromWarehouseUseCase;

    @MockitoBean
    private DeleteWarehouseUseCase deleteWarehouseUseCase;

    private Warehouse sampleWarehouse() {
        return Warehouse.reconstitute(1L, "Main Warehouse", List.of());
    }

    @Test
    void shouldCreateWarehouse() throws Exception {

        CreateWarehouseRequest request = new CreateWarehouseRequest("Main Warehouse");

        when(createWarehouseUseCase.execute("Main Warehouse")).thenReturn(sampleWarehouse());

        mockMvc.perform(post("/api/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Main Warehouse"));

        verify(createWarehouseUseCase).execute("Main Warehouse");
    }

    @Test
    void shouldRejectCreateWarehouseWithBlankName() throws Exception {

        CreateWarehouseRequest request = new CreateWarehouseRequest("");

        mockMvc.perform(post("/api/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnWarehouseById() throws Exception {

        when(getWarehouseUseCase.execute(1L)).thenReturn(sampleWarehouse());

        mockMvc.perform(get("/api/warehouses/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Main Warehouse"));
    }

    @Test
    void shouldReturn404WhenWarehouseNotFound() throws Exception {

        when(getWarehouseUseCase.execute(99L))
                .thenThrow(new IllegalArgumentException("Warehouse not found"));

        mockMvc.perform(get("/api/warehouses/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllWarehouses() throws Exception {

        when(getWarehousesUseCase.execute()).thenReturn(List.of(sampleWarehouse()));

        mockMvc.perform(get("/api/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Main Warehouse"));
    }

    @Test
    void shouldReturnWarehouseItems() throws Exception {

        Item item = Item.reconstitute(
                1L, "Old Printer", "Desc", ItemStatus.IN_USE, ItemType.ELECTRONICS, 8,
                LocalDate.now(), null, null, null, null, null
        );

        when(getWarehouseItemsUseCase.execute(1L)).thenReturn(List.of(item));

        mockMvc.perform(get("/api/warehouses/{id}/items", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Old Printer"));
    }

    @Test
    void shouldAddItemToWarehouse() throws Exception {

        AddItemToWarehouseRequest request = new AddItemToWarehouseRequest(5L);

        mockMvc.perform(post("/api/warehouses/{id}/items", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(addItemToWarehouseUseCase).execute(1L, 5L);
    }

    @Test
    void shouldRemoveItemFromWarehouse() throws Exception {

        mockMvc.perform(delete("/api/warehouses/{id}/items/{itemId}", 1L, 5L))
                .andExpect(status().isNoContent());

        verify(removeItemFromWarehouseUseCase).execute(1L, 5L);
    }

    @Test
    void shouldChangeWarehouseName() throws Exception {

        ChangeWarehouseNameRequest request = new ChangeWarehouseNameRequest("New Name");

        mockMvc.perform(patch("/api/warehouses/{id}/name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(changeWarehouseNameUseCase).execute(1L, "New Name");
    }

    @Test
    void shouldDeleteWarehouse() throws Exception {

        mockMvc.perform(delete("/api/warehouses/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteWarehouseUseCase).execute(1L);
    }

    @Test
    void shouldReturn404WhenDeletingMissingWarehouse() throws Exception {

        doThrow(new IllegalArgumentException("Warehouse not found"))
                .when(deleteWarehouseUseCase).execute(99L);

        mockMvc.perform(delete("/api/warehouses/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
