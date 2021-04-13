package com.hys.blog.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hys.blog.provider.security.JwtAuthTokenProvider;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
	
	private JwtAuthTokenProvider jwtAuthTokenProvider;
	
	public JwtConfigurer(JwtAuthTokenProvider jwtAuthTokenProvider) {
        this.jwtAuthTokenProvider = jwtAuthTokenProvider;
    }

	@Override
	public void configure(HttpSecurity builder) throws Exception {
		JwtFilter customFilter = new JwtFilter(jwtAuthTokenProvider);
		builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	
}
