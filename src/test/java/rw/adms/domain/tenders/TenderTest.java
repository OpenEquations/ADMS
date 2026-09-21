package rw.adms.domain.tenders;

import org.junit.jupiter.api.Test;
import rw.adms.domain.tenders.enums.TenderStatus;
import rw.adms.domain.tenders.enums.TenderType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TenderTest {

    @Test
    void shouldRejectPublishingWithoutADeadline() {

        Tender tender = new Tender(
                "Office equipment tender",
                "Selling old office equipment",
                TenderType.SELLING_TENDER,
                null
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tender.changeTenderStatus(TenderStatus.PUBLISHED)
        );

        assertEquals(
                "Tender cannot be published without a deadline",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowPublishingOnceADeadlineIsSet() {

        Tender tender = new Tender(
                "Office equipment tender",
                "Selling old office equipment",
                TenderType.SELLING_TENDER,
                null
        );

        tender.setDeadline(LocalDateTime.now().plusDays(7));

        assertDoesNotThrow(
                () -> tender.changeTenderStatus(TenderStatus.PUBLISHED)
        );

        assertEquals(TenderStatus.PUBLISHED, tender.getStatus());
    }

    @Test
    void shouldAllowNonPublishedTransitionsWithoutADeadline() {

        Tender tender = new Tender(
                "Office equipment tender",
                "Selling old office equipment",
                TenderType.SELLING_TENDER,
                null
        );

        assertDoesNotThrow(
                () -> tender.changeTenderStatus(TenderStatus.OVER)
        );
    }
}
