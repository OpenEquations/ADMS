package rw.adms.domain.auth;

import java.time.LocalDateTime;

/**
 * An issued bearer token for a logged-in user. The token itself is the
 * credential - anyone holding it is treated as that user until it expires
 * or is explicitly revoked (logout).
 */
public class Session {

    private final String token;
    private final Long userId;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;

    public Session(String token, Long userId, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired(LocalDateTime now) {
        return now.isAfter(expiresAt);
    }
}
