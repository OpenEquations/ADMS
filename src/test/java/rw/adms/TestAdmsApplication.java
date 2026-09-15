package rw.adms;

import org.springframework.boot.SpringApplication;

public class TestAdmsApplication {

	public static void main(String[] args) {
		SpringApplication.from(AdmsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
