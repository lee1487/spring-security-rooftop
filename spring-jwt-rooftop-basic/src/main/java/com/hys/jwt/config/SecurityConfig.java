package com.hys.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.jwt.service.PrincipalDetailsService;
import com.hys.jwt.service.UserService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final PrincipalDetailsService principalDetailsService;
	private  final UserService userService;
	private final ObjectMapper mapper;
	private final JwtProperties jwtProperties;
	
	private JWTUtil jwtUtil;
	
	public SecurityConfig(UserService userService, ObjectMapper mapper, JwtProperties jwtProperties,PrincipalDetailsService principalDetailsService) {
		this.userService = userService;
		this.mapper = mapper;
		this.principalDetailsService = principalDetailsService;
		this.jwtProperties = jwtProperties;
		this.jwtUtil = new JWTUtil(jwtProperties);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
	}
	
	

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager(), jwtUtil, mapper);
		JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService, jwtUtil);
		http
			.csrf().disable()
			.addFilter(jwtLoginFilter)
			.addFilter(checkFilter)
		;
	}
	
	
	
}
