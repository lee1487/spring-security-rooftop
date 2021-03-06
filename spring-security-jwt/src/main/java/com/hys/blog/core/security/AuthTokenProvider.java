package com.hys.blog.core.security;

import java.util.Date;

import org.springframework.security.core.Authentication;

public interface AuthTokenProvider<T> {
	T createAuthToken(String id, String role, Date expiredDate);
	T convertAuthToken(String token);
	Authentication getAuthentication(T authToken);
}
