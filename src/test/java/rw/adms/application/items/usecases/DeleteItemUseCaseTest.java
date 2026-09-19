package rw.adms.application.items.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.items.interfaces.ItemRepository;
import rw.adms.domain.items.vo.ItemId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteItemUseCaseTest {

    @Test
    void shouldDeleteItem() {

        // Arrange
        ItemRepository itemRepository = mock(ItemRepository.class);

        when(itemRepository.existsById(new ItemId(1L))).thenReturn(true);

        DeleteItemUseCase useCase = new DeleteItemUseCase(itemRepository);

        // Act
        useCase.execute(1L);

        // Assert
        verify(itemRepository).deleteById(new ItemId(1L));
    }

    @Test
    void shouldThrowExceptionWhenItemDoesNotExist() {

        // Arrange
        ItemRepository itemRepository = mock(ItemRepository.class);

        when(itemRepository.existsById(new ItemId(1L))).thenReturn(false);

        DeleteItemUseCase useCase = new DeleteItemUseCase(itemRepository);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L)
        );

        assertEquals("Item not found", exception.getMessage());

        verify(itemRepository, never()).deleteById(any());
    }
}
