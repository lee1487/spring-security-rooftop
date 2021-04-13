package com.hys.jwt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.hys.jwt.service.UserService;
import com.hys.jwt.vo.PrincipalDetails;
import com.hys.jwt.vo.User;
import com.hys.jwt.vo.dto.VerifyResultDto;

public class JWTCheckFilter extends BasicAuthenticationFilter{

	private final UserService userService;
	private final JWTUtil jwtUtil;
	
	public JWTCheckFilter(AuthenticationManager authenticationManager,UserService userService,JWTUtil jwtUtil) {
		super(authenticationManager);
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain) throws IOException, ServletException 
	{
		String token = request.getHeader(JWTUtil.AUTH_HEADER);
		if (token == null || !token.startsWith(JWTUtil.BEARER)) {
			chain.doFilter(request, response);
			return;
		}
		
		VerifyResultDto result = jwtUtil.verify(token.substring(JWTUtil.BEARER.length()));
		if (result.isResult()) {
			User user = userService.findUser(result.getId()).get();
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities()); 
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}
	
	

	
}
