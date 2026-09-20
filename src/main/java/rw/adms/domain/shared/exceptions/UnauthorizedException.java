package rw.adms.domain.shared.exceptions;

/**
 * Thrown when a request requires an authenticated actor but the bearer token
 * is missing, invalid, or expired - maps to HTTP 401.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
