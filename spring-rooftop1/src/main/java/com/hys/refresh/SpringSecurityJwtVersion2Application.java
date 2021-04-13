package com.hys.refresh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityJwtVersion2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtVersion2Application.class, args);
	}

}
