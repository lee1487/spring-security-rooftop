package com.hys.authority.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hys.authority.vo.SecurityMessage;

@RestController
public class TestController {
	
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/user")
	public SecurityMessage user() {
		return SecurityMessage.builder()
				.message("user page")
				.auth(SecurityContextHolder.getContext().getAuthentication()).build();
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	@GetMapping("/admin")
	public SecurityMessage admin() {
		return SecurityMessage.builder()
				.message("admin page")
				.auth(SecurityContextHolder.getContext().getAuthentication()).build();
	}
}
