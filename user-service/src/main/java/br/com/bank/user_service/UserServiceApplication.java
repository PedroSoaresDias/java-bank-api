package br.com.bank.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.github.cdimascio.dotenv.Dotenv;

@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
		System.setProperty("PG_URL_USER", dotenv.get("PG_URL_USER"));
		System.setProperty("PG_USER", dotenv.get("PG_USER"));
		System.setProperty("PG_PASSWORD", dotenv.get("PG_PASSWORD"));

		SpringApplication.run(UserServiceApplication.class, args);
	}

}
