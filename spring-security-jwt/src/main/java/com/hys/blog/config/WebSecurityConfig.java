package com.hys.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hys.blog.core.security.Role;
import com.hys.blog.exception.JwtAccessDeniedHandler;
import com.hys.blog.exception.JwtAuthenticationEntryPoint;
import com.hys.blog.provider.security.JwtAuthTokenProvider;
import com.hys.blog.security.JwtConfigurer;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private final JwtAuthTokenProvider jwtAuthTokenProvider;
	private final JwtAuthenticationEntryPoint authenticationErrorHandler;
	private final JwtAccessDeniedHandler jwtAccessDenviedHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//csrf 설정 사용 안함
			.csrf().disable()
			
			// exception설정
			.exceptionHandling()
			.authenticationEntryPoint(authenticationErrorHandler)
			.accessDeniedHandler(jwtAccessDenviedHandler)
			
			// 동일 도메인에서는 iframe 접근이 가능하도록 한 설정
			.and()
			.headers()
			.frameOptions()
			.sameOrigin()
			
			// 세션 사용하지 않겠다는 설정
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			
			.and()
			.authorizeRequests()
			.antMatchers("/api/v1/login/**").permitAll()
			
			.antMatchers("/api/v1/coffees/**").hasAnyAuthority(Role.USER.getCode())
			.anyRequest().authenticated()
			
			.and()
			.apply(securityConfigurerAdapter());
			
	}
	
	private JwtConfigurer securityConfigurerAdapter() {
		return new JwtConfigurer(jwtAuthTokenProvider);
	}
	
	
}
