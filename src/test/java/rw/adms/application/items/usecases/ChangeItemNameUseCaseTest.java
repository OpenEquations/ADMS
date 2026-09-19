package rw.adms.application.items.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ChangeItemNameUseCaseTest {

    @Test
    void shouldChangeItemName() {

        // Arrange
        ItemRepository itemRepository = mock(ItemRepository.class);

        Item item = Item.reconstitute(
                1L,
                "Old Printer",
                "An old office printer",
                ItemStatus.IN_USE,
                ItemType.ELECTRONICS,
                8,
                LocalDate.now(),
                null,
                null,
                null,
                null,
                null
        );

        when(itemRepository.findById(new ItemId(1L)))
                .thenReturn(Optional.of(item));

        ChangeItemNameUseCase useCase = new ChangeItemNameUseCase(itemRepository);

        // Act
        useCase.execute(1L, "New Printer");

        // Assert
        assertEquals("New Printer", item.getItemName());

        verify(itemRepository).save(item);
    }

    @Test
    void shouldThrowExceptionWhenItemDoesNotExist() {

        ItemRepository itemRepository = mock(ItemRepository.class);

        when(itemRepository.findById(new ItemId(1L)))
                .thenReturn(Optional.empty());

        ChangeItemNameUseCase useCase = new ChangeItemNameUseCase(itemRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, "New Printer")
        );

        verify(itemRepository, never()).save(any());
    }
}
