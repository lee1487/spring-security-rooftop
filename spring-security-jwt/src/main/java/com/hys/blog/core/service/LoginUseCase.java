package com.hys.blog.core.service;

import java.util.Optional;

import com.hys.blog.core.security.AuthToken;

public interface LoginUseCase {
	Optional<MemberDTO> login(String id, String password);
	AuthToken createAuthToken(MemberDTO memberDTO);
}
