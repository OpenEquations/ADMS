package rw.adms.presentation.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rw.adms.domain.shared.exceptions.ForbiddenException;
import rw.adms.domain.shared.exceptions.UnauthorizedException;

import java.util.List;
import java.util.Locale;

/**
 * Translates exceptions bubbling up from the application/domain layers into
 * HTTP responses. This is the one place in the codebase allowed to know that
 * "IllegalArgumentException" means either "bad input" or "not found" -
 * everything below the presentation layer stays HTTP-agnostic.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {

        HttpStatus status = isNotFound(ex.getMessage())
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiError.of(
                        status.value(),
                        status.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of(
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        ex.getMessage()
                ));
    }

    /**
     * Catches raw SQL constraint failures (e.g. deleting a row another table
     * still has a foreign key pointing at) that slip past the application
     * layer, so the client gets a clean, actionable message instead of a
     * leaked SQL exception and a 500.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.of(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "This action could not be completed because the record is still "
                                + "referenced elsewhere in the system. Remove those references first."
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ApiError.of(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Validation failed",
                        details
                ));
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    private boolean isNotFound(String message) {
        return message != null
                && message.toLowerCase(Locale.ROOT).contains("not found");
    }
}
