package com.bookshop.profile;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ProfileApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("profile-service")
				.filename(".env")
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);
		SpringApplication.run(ProfileApplication.class, args);
	}

}
