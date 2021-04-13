package com.hys.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles("test")
class SpringUserModuleApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(SpringUserModuleApplicationTests.class, args);
	}

}
