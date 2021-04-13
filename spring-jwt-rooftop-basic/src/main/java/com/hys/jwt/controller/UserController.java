package com.hys.jwt.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hys.jwt.service.UserService;
import com.hys.jwt.vo.RestResponsePage;
import com.hys.jwt.vo.User;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

	private final UserService userService;
	
	// save
	@PostMapping("/save")
	public User saveUser(@RequestBody User user) {
		return userService.save(user);
	}
	
	@GetMapping("/{id}")
	public Optional<User> getUser(@PathVariable Long id) {
		return userService.findUser(id);
	}
	
	// list : page
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	@GetMapping("/list")
	public Page<User> list (
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size
	) {
		return RestResponsePage.of(userService.listUsers(page, size));
	}
	
	// add role
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	@PutMapping("/authority/add")
	public Optional<User> addAuthority(
			@RequestParam Long id,
			@RequestParam String authority
	) {
		
		userService.findUser(id).ifPresent(entity -> {
			userService.addAuthority(id, authority);
		});
		return userService.findUser(id); 
		
	}
	
	// remove role
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	@PutMapping("/authority/remove")
	public Optional<User> removeAuthority (
			@RequestParam Long id,
			@RequestParam String authority
	) {
		
		userService.findUser(id).ifPresent(entity -> {
			try {
				userService.removeAuthority(id, authority);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		});
		return userService.findUser(id); 
		
	}
}
