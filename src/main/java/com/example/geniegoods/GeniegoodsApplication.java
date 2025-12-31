package com.example.geniegoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GeniegoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeniegoodsApplication.class, args);
	}

}
