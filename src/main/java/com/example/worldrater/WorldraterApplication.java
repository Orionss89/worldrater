package com.example.worldrater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorldraterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldraterApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner demo(
			com.example.worldrater.repository.UserRepository userRepository,
			org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
		return (args) -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				com.example.worldrater.model.User admin = new com.example.worldrater.model.User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ROLE_ADMIN");
				userRepository.save(admin);
				System.out.println("ADMIN user created: admin / admin123");
			}
		};
	}

}
