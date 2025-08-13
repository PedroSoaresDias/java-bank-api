package br.com.bank.java_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import io.github.cdimascio.dotenv.Dotenv;

@EnableR2dbcRepositories
@SpringBootApplication
public class JavaBankApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
		System.setProperty("PG_URL", dotenv.get("PG_URL"));
		System.setProperty("PG_USER", dotenv.get("PG_USER"));
		System.setProperty("PG_PASSWORD", dotenv.get("PG_PASSWORD"));

		SpringApplication.run(JavaBankApplication.class, args);
	}

}
