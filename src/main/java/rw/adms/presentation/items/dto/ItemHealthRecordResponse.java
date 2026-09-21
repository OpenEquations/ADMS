package rw.adms.presentation.items.dto;

import rw.adms.domain.items.ItemHealthRecord;

import java.time.LocalDateTime;

public record ItemHealthRecordResponse(
        int health,
        LocalDateTime recordedAt
) {

    public static ItemHealthRecordResponse from(ItemHealthRecord record) {
        return new ItemHealthRecordResponse(record.getHealth(), record.getRecordedAt());
    }
}
