package rw.adms.application.tenders.usecases;

import org.junit.jupiter.api.Test;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.enums.ItemStatus;
import rw.adms.domain.items.enums.ItemType;
import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;
import rw.adms.domain.tenders.interfaces.TenderRepository;
import rw.adms.domain.tenders.vo.TenderId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetTenderItemsUseCaseTest {

    @Test
    void shouldReturnTenderItems() {

        // Arrange
        TenderRepository tenderRepository = mock(TenderRepository.class);

        Item item = Item.reconstitute(
                1L,
                "Old Printer",
                "An old office printer",
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

        Tender tender = Tender.reconstitute(
                1L,
                "Office equipment tender",
                "Selling old office equipment",
                List.of(item),
                TenderType.SELLING_TENDER,
                null,
                TenderStatus.PUBLISHED
        );

        when(tenderRepository.findById(new TenderId(1L)))
                .thenReturn(Optional.of(tender));

        GetTenderItemsUseCase useCase = new GetTenderItemsUseCase(tenderRepository);

        // Act
        List<Item> result = useCase.execute(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }

    @Test
    void shouldThrowExceptionWhenTenderDoesNotExist() {

        TenderRepository tenderRepository = mock(TenderRepository.class);

        when(tenderRepository.findById(new TenderId(1L)))
                .thenReturn(Optional.empty());

        GetTenderItemsUseCase useCase = new GetTenderItemsUseCase(tenderRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L)
        );
    }
}
