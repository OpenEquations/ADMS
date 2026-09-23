package rw.adms.application.items.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemHealthRecordRepository;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemHealth;
import rw.adms.domain.items.vo.ItemId;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.warehouses.Warehouse;
import rw.adms.domain.warehouses.interfaces.WarehouseRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteItemUseCaseTest {

    private Item sampleItem() {
        return Item.reconstitute(
                1L,
                "Old Printer",
                "Desc",
                ItemStatus.NO_LONGER_IN_USE,
                ItemType.ELECTRONICS,
                3,
                LocalDate.now(),
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void shouldDeleteItemWithNoAssociations() {

        ItemRepository itemRepository = mock(ItemRepository.class);
        WarehouseRepository warehouseRepository = mock(WarehouseRepository.class);
        TenderRepository tenderRepository = mock(TenderRepository.class);
        ItemHealthRecordRepository itemHealthRecordRepository = mock(ItemHealthRecordRepository.class);

        when(itemRepository.existsById(new ItemId(1L))).thenReturn(true);
        when(warehouseRepository.findAll()).thenReturn(List.of());
        when(tenderRepository.findAll()).thenReturn(List.of());

        DeleteItemUseCase useCase = new DeleteItemUseCase(
                itemRepository, warehouseRepository, tenderRepository, itemHealthRecordRepository
        );

        useCase.execute(1L);

        verify(itemHealthRecordRepository).deleteByItemId(1L);
        verify(itemRepository).deleteById(new ItemId(1L));
    }

    @Test
    void shouldDetachItemFromWarehousesAndTendersBeforeDeleting() {

        ItemRepository itemRepository = mock(ItemRepository.class);
        WarehouseRepository warehouseRepository = mock(WarehouseRepository.class);
        TenderRepository tenderRepository = mock(TenderRepository.class);
        ItemHealthRecordRepository itemHealthRecordRepository = mock(ItemHealthRecordRepository.class);

        when(itemRepository.existsById(new ItemId(1L))).thenReturn(true);

        Warehouse warehouseWithItem = new Warehouse("Main Warehouse");
        warehouseWithItem.addItem(sampleItem());
        Warehouse emptyWarehouse = new Warehouse("Empty Warehouse");
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouseWithItem, emptyWarehouse));

        Tender tenderWithItem = new Tender("Selling tender", "desc", TenderType.SELLING_TENDER, null);
        tenderWithItem.addItem(sampleItem());
        Tender emptyTender = new Tender("Other tender", "desc", TenderType.SELLING_TENDER, null);
        when(tenderRepository.findAll()).thenReturn(List.of(tenderWithItem, emptyTender));

        DeleteItemUseCase useCase = new DeleteItemUseCase(
                itemRepository, warehouseRepository, tenderRepository, itemHealthRecordRepository
        );

        useCase.execute(1L);

        assertEquals(0, warehouseWithItem.getItems().size());
        verify(warehouseRepository).save(warehouseWithItem);
        verify(warehouseRepository, never()).save(emptyWarehouse);

        assertEquals(0, tenderWithItem.getItems().size());
        verify(tenderRepository).save(tenderWithItem);
        verify(tenderRepository, never()).save(emptyTender);

        verify(itemHealthRecordRepository).deleteByItemId(1L);
        verify(itemRepository).deleteById(new ItemId(1L));
    }

    @Test
    void shouldThrowExceptionWhenItemDoesNotExist() {

        ItemRepository itemRepository = mock(ItemRepository.class);
        WarehouseRepository warehouseRepository = mock(WarehouseRepository.class);
        TenderRepository tenderRepository = mock(TenderRepository.class);
        ItemHealthRecordRepository itemHealthRecordRepository = mock(ItemHealthRecordRepository.class);

        when(itemRepository.existsById(new ItemId(1L))).thenReturn(false);

        DeleteItemUseCase useCase = new DeleteItemUseCase(
                itemRepository, warehouseRepository, tenderRepository, itemHealthRecordRepository
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L)
        );

        assertEquals("Item not found", exception.getMessage());

        verify(itemRepository, never()).deleteById(any());
        verifyNoInteractions(warehouseRepository, tenderRepository, itemHealthRecordRepository);
    }
}
