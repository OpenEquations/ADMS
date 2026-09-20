package rw.adms.presentation.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import rw.adms.application.auth.usecases.ValidateSessionUseCase;
import rw.adms.domain.shared.exceptions.UnauthorizedException;
import rw.adms.domain.users.User;

import java.util.Optional;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ValidateSessionUseCase validateSessionUseCase;

    public CurrentUserArgumentResolver(ValidateSessionUseCase validateSessionUseCase) {
        this.validateSessionUseCase = validateSessionUseCase;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && User.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        CurrentUser annotation = parameter.getParameterAnnotation(CurrentUser.class);
        boolean required = annotation == null || annotation.required();

        String token = extractToken(webRequest.getNativeRequest(HttpServletRequest.class));
        Optional<User> user = validateSessionUseCase.execute(token);

        if (user.isEmpty() && required) {
            throw new UnauthorizedException("Authentication required");
        }

        return user.orElse(null);
    }

    private String extractToken(HttpServletRequest request) {

        if (request == null) {
            return null;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return header.substring(BEARER_PREFIX.length()).trim();
    }
}
