package com.hys.jwt;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.jwt.config.JWTUtil;
import com.hys.jwt.repository.UserRepository;
import com.hys.jwt.service.UserService;
import com.hys.jwt.vo.RestResponsePage;
import com.hys.jwt.vo.User;
import com.hys.jwt.vo.dto.UserLoginDto;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
	
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
	
	private User USER1;
	private User ADMIN;

	private URI uri(String path) throws URISyntaxException {
		URI uri = new URI(format("http://localhost:%d%s", port, path));
		System.out.println("uri: " + uri);
		return uri;
	}
	
	@BeforeEach
	void before() {
		userService.deleteAll();
		
		this.userTestHelper = new UserTestHelper(userService, passwordEncoder);
		this.USER1 = this.userTestHelper.createUser("user1", "ROLE_USER");
		this.ADMIN = this.userTestHelper.createUser("admin", "ROLE_ADMIN");
		
		System.out.println("db에 데이터 삽입");
	}
	
	@AfterEach
	void after() {
		//userRepository.deleteAll();
	}
	
	private String getToken(String username, String password) throws URISyntaxException{
		UserLoginDto login = UserLoginDto.builder().username(username).password(password).build();
		HttpEntity<UserLoginDto> body = new HttpEntity<UserLoginDto>(login);
		
		
		ResponseEntity<String> response =  restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
		assertEquals(200, response.getStatusCodeValue());
		return response.getHeaders().get(JWTUtil.AUTH_HEADER).get(0).substring(JWTUtil.BEARER.length());
	}
	
	private HttpEntity getAuthHeaderEntity(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(JWTUtil.AUTH_HEADER, JWTUtil.BEARER + accessToken);
		HttpEntity entity = new HttpEntity("", headers);
		return entity;
	}
	
	@DisplayName("1-1. user1은 자신의 정보를 조회할 수 있다.")
	@Test
	void test_1_1() throws URISyntaxException, JsonProcessingException{
		String accessToken = getToken("user1@test.com", "user11234");
		ResponseEntity<User> response =  restTemplate.exchange(uri("/user/" + USER1.getId()),
				HttpMethod.GET, getAuthHeaderEntity(accessToken), User.class);
		
		assertEquals(200, response.getStatusCodeValue());
		userTestHelper.assertUser(response.getBody(), "user1");
		
	}
	
	@DisplayName("1. admin 유저는 userList를 가져올 수 있다.")
	@Test
	void test_1() throws URISyntaxException, JsonProcessingException{
		String accessToken = getToken("admin@test.com", "admin1234");
		ResponseEntity<String> response =  restTemplate.exchange(uri("/user/list"),
				HttpMethod.GET, getAuthHeaderEntity(accessToken), String.class);
		
		RestResponsePage<User> page = mapper.readValue(response.getBody(),
				new TypeReference<RestResponsePage<User>>() {
				});
		
		assertEquals(2, page.getTotalElements());
		List<String> list = new ArrayList<>();
		list.add("user1");
		list.add("admin");
		assertTrue(page.getContent().stream().map(user -> user.getName())
				.collect(Collectors.toList()).containsAll(list));
		 
		page.getContent().forEach(System.out::println);
	}

	
	@DisplayName("2. user1에게 admin 권한을 준다.")
	@Test
	void test_2() throws URISyntaxException, JsonProcessingException {
		
		// token
		String accessToken = getToken("admin@test.com", "admin1234");
		
		// user1에게 admin권한을 준다. 
		ResponseEntity<String> response =  restTemplate.exchange(uri(
				format("/user/authority/add?id=%d&authority=%s", USER1.getId(), "ROLE_ADMIN")),
				HttpMethod.PUT, getAuthHeaderEntity(accessToken), String.class);
		
		assertEquals(200, response.getStatusCodeValue());
		
		// user1 데이터를 가져와서 확인한다.
		ResponseEntity<String> response2 =  restTemplate.exchange(uri(
				format("/user/%d", USER1.getId())),
				HttpMethod.GET, getAuthHeaderEntity(accessToken), String.class);
		
		assertEquals(200, response2.getStatusCodeValue());
		
		User resUser = mapper.readValue(response2.getBody(), User.class);
		assertTrue(resUser.getRoleList().contains("ROLE_ADMIN"));
	}
}
