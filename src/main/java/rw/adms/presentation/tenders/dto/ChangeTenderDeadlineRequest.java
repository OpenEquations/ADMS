package rw.adms.presentation.tenders.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ChangeTenderDeadlineRequest(

        @NotNull(message = "Deadline is required")
        LocalDateTime deadline
) {
}
