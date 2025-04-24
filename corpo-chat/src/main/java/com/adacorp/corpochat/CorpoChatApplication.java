package com.adacorp.corpochat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CorpoChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorpoChatApplication.class, args);
	}

}
