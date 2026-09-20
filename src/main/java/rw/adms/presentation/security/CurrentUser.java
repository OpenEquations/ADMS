package rw.adms.presentation.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Resolves a controller parameter to the User identified by the request's
 * {@code Authorization: Bearer <token>} header.
 * <p>
 * By default the token must be present and valid, or the request is
 * rejected with 401 before the controller method runs. Set
 * {@code required = false} for the one endpoint that legitimately has no
 * actor yet - bootstrapping the very first user - where the parameter is
 * {@code null} instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CurrentUser {

    boolean required() default true;
}
