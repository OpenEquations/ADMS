package rw.adms.domain.shared.exceptions;

/**
 * Thrown when an authenticated actor is known but lacks the role/permission
 * required for the action - maps to HTTP 403.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
