package rw.adms.presentation.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Lives under {@code rw.adms.presentation} (rather than alongside the
 * app-wide {@code WebConfig} in {@code infrastructure}) so that
 * {@code @WebMvcTest} slices - anchored by {@code PresentationTestConfig},
 * which only scans this package tree - pick it up and actually exercise
 * {@code @CurrentUser} resolution instead of silently ignoring the
 * annotation.
 */
@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    public ArgumentResolverConfig(CurrentUserArgumentResolver currentUserArgumentResolver) {
        this.currentUserArgumentResolver = currentUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }
}
