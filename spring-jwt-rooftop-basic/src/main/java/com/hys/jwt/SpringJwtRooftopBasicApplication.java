package com.hys.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hys.jwt.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class SpringJwtRooftopBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtRooftopBasicApplication.class, args);
	}

}
