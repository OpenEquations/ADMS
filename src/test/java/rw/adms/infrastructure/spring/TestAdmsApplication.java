package rw.adms.infrastructure.spring;

import org.springframework.boot.SpringApplication;
import rw.adms.TestcontainersConfiguration;

public class TestAdmsApplication {

	public static void main(String[] args) {
		SpringApplication.from(AdmsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
