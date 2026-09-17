package rw.adms.infrastructure.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = "rw.adms"
)
@EnableJpaRepositories(
        basePackages = "rw.adms.infrastructure.persistence.repositories"
)
public class AdmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                AdmsApplication.class,
                args
        );
    }
}