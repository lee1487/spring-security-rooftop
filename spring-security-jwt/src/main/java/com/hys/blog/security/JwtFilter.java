package com.hys.blog.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.hys.blog.provider.security.JwtAuthToken;
import com.hys.blog.provider.security.JwtAuthTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends GenericFilterBean{
	
	private static final String AUTHORIZATION_HEADER = "x-auth-token";
	
	private JwtAuthTokenProvider jwtAuthTokenProvider;
	
	JwtFilter(JwtAuthTokenProvider jwtAuthTokenProvider) {
	      this.jwtAuthTokenProvider = jwtAuthTokenProvider;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//request에서 토큰이 존재하면 Optional<String> token에 저장됨 
		HttpServletRequest req = (HttpServletRequest) request;
		Optional<String> token = resolveToken(req);
		
		// 무조건 api호출시 여기로 오는데 로그인 시에는 token이 없으니 그냥 필터에 걸러짐 
		if (token.isPresent()) {
			JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
			
			if (jwtAuthToken.validate()) {
				Authentication authentication = jwtAuthTokenProvider.getAuthentication(jwtAuthToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
	
	private Optional<String> resolveToken(HttpServletRequest request) {
		String authToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(authToken)) {
			return Optional.of(authToken);
		} else {
			return Optional.empty();
		}
	}
	
}
