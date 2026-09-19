package rw.adms.presentation;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal {@code @SpringBootConfiguration} anchor for {@code @WebMvcTest}
 * slices under this package.
 * <p>
 * {@code @WebMvcTest} searches upward from the test's package for a
 * {@code @SpringBootConfiguration} to bootstrap from, and needs a
 * {@code @ComponentScan} in scope for its controller-only scanning filter to
 * find anything at all. The real application class ({@code AdmsApplication})
 * lives outside this package tree and also carries
 * {@code @EnableJpaRepositories}, which isn't filtered out by the web test
 * slice and would otherwise pull in JPA repository beans that need a
 * {@code DataSource}. This class gives web-layer tests a lightweight,
 * JPA-free anchor instead, scoped to {@code rw.adms.presentation} and below.
 */
@SpringBootApplication
class PresentationTestConfig {
}
