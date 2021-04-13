package com.hys.jwt;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.jwt.config.JWTUtil;
import com.hys.jwt.repository.UserRepository;
import com.hys.jwt.service.UserService;
import com.hys.jwt.vo.User;
import com.hys.jwt.vo.dto.UserLoginDto;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JWTLoginFilterTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired   
	private ObjectMapper mapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private UserTestHelper userTestHelper;
	
	
	private RestTemplate restTemplate = new RestTemplate();

	private URI uri(String path) throws URISyntaxException {
		return new URI(format("http://localhost:%d%s", port, path));
	}
	
	@BeforeEach
	void before() {
		userService.deleteAll();
		this.userTestHelper = new UserTestHelper(userService, passwordEncoder);
		User user1 = this.userTestHelper.createUser("user1", "ROLE_USER"); 
		System.out.println("시작");
		
	}
	
	@DisplayName("1. jwt로 로그인을 시도한다.")
	@Test
	void test_1() throws URISyntaxException {
		
		UserLoginDto login = UserLoginDto.builder().username("user1@test.com").password("user11234").build();
		HttpEntity<UserLoginDto> body = new HttpEntity<UserLoginDto>(login);
		ResponseEntity<String> response =  restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
		
		assertEquals(200, response.getStatusCodeValue());
		
		System.out.println(response.getHeaders().get(JWTUtil.AUTH_HEADER));
	}
	
	@DisplayName("2. 비번이 틀리면 로그인을 하지 못한다.")
	@Test
	void test_2() throws URISyntaxException {
		
		UserLoginDto login = UserLoginDto.builder().username("user1@test.com").password("1234").build();
		HttpEntity<UserLoginDto> body = new HttpEntity<UserLoginDto>(login);
		
		assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
			// expected 401 error
		});
		
	}
	
}
