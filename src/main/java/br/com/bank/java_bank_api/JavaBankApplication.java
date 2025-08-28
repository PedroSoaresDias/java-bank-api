package br.com.bank.java_bank_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class JavaBankApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
			.filename(".env")
			.ignoreIfMissing()
			.load();

		String env = dotenv.get("SPRING_ENV", "local");

		if (env.equals("docker")) {
			System.setProperty("PG_URL", dotenv.get("PG_URL_DOCKER"));
		} else {
			System.setProperty("PG_URL", dotenv.get("PG_URL"));
		}

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
		System.setProperty("PG_USER", dotenv.get("PG_USER"));
		System.setProperty("PG_PASSWORD", dotenv.get("PG_PASSWORD"));

		SpringApplication.run(JavaBankApplication.class, args);
	}

}
