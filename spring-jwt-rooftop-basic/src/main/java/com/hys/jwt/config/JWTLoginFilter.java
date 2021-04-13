package com.hys.jwt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.jwt.vo.PrincipalDetails;
import com.hys.jwt.vo.dto.UserLoginDto;

import lombok.SneakyThrows;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager; 
	private final JWTUtil jwtUtil;
	private final ObjectMapper mapper;
	
	public JWTLoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil, ObjectMapper mapper) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil; 
		this.mapper = mapper;
		setFilterProcessesUrl("/login");
	}

	@SneakyThrows
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		UserLoginDto loginDto = mapper.readValue(request.getInputStream(), UserLoginDto.class);
		System.out.println("loginDto: "+loginDto);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), null);
		Authentication authentication = authenticationManager.authenticate(authToken);
		return authentication;
	}
	
	@Override
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException 
	{	
		PrincipalDetails principalDetails  = (PrincipalDetails) authResult.getPrincipal();
		response.addHeader(JWTUtil.AUTH_HEADER, JWTUtil.BEARER + jwtUtil.generate(principalDetails.getUser().getId().toString()));
		
		//super.successfulAuthentication(request, response, chain, authResult);
	}

	
}
