package com.hys.blog.web;

import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hys.blog.core.CommonResponse;
import com.hys.blog.core.service.MemberDTO;
import com.hys.blog.exception.LoginFailedException;
import com.hys.blog.provider.security.JwtAuthToken;
import com.hys.blog.provider.service.LoginService;
import com.hys.blog.web.dto.LoginRequestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
	
	@PostMapping
	public CommonResponse login (@RequestBody LoginRequestDTO loginRequestDTO) {
		
		Optional<MemberDTO> optionalMemberDTO = loginService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
		
		if (optionalMemberDTO.isPresent()) {

            JwtAuthToken jwtAuthToken = (JwtAuthToken) loginService.createAuthToken(optionalMemberDTO.get());

            return CommonResponse.builder()
                    .code("LOGIN_SUCCESS")
                    .status(200)
                    .message(jwtAuthToken.getToken())
                    .build();

        } else {
            throw new LoginFailedException();
        }
	}
}
