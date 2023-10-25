package com.lcwd.electronic.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication

public class ElectronicStoreApplication {

	public static void main(String[] args) {

		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

}
