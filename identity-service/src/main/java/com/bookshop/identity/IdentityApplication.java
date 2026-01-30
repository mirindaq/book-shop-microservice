package com.bookshop.identity;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IdentityApplication {


	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
				.directory("identity-service")
				.filename(".env")
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);

		SpringApplication.run(IdentityApplication.class, args);
	}

}
