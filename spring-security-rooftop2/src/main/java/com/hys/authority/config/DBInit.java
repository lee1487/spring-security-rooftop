package com.hys.authority.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hys.authority.service.UserService;
import com.hys.authority.vo.User;

@Component
public class DBInit implements CommandLineRunner{
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		User user1 = User.builder().name("user2")
					.email("user2@test.com")
					.password(passwordEncoder.encode("1234"))
					.enabled(true)
					.roles("ROLE_USER")
					.build();
		
		User admin = User.builder().name("admin")
				.email("admin@test.com")
				.password(passwordEncoder.encode("admin"))
				.enabled(true)
				.roles("ROLE_ADMIN")
				.build();
		
		userService.save(user1);
		userService.save(admin);
		
	}
	 
	 
}
