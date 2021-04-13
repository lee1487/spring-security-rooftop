package com.hys.authority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static java.lang.String.format;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.authority.service.PrincipalDetailsService;
import com.hys.authority.service.UserService;
import com.hys.authority.vo.PrincipalDetails;
import com.hys.authority.vo.RestResponsePage;
import com.hys.authority.vo.User;

@WebMvcTest
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PrincipalDetailsService principalDetailsService;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private ObjectMapper mapper;
	
	PrincipalDetails user2() {
		User user2 = User.builder()
						.id(1L)
						.email("user2@test.com")
						.name("user2")
						.roles("ROLE_USER")
						.enabled(true)
						.build();
					
		
		return  new PrincipalDetails(user2);
	}
	
	PrincipalDetails admin() {
		User admin = User.builder()
				.id(2L)
				.email("admin@test.com")
				.name("admin")
				.roles("ROLE_ADMIN")
				.enabled(true)
				.build();
			

		return  new PrincipalDetails(admin);

	}
	
	@DisplayName("1. 리스트 접근은 권한이 있어야 한다.")
	@Test
	void test_1() throws Exception {
		mockMvc.perform(get("/user/list"))
				.andExpect(status().is3xxRedirection());
	}
	
	@DisplayName("2. admin은 user list에 접근할 수 있다.")
	@Test
	void test_2() throws Exception{
		when(userService.listUsers(1, 10)).thenReturn(new RestResponsePage(user2(), admin()));
		mockMvc.perform(get("/user/list").with(user(admin())))
							.andExpect(status().isOk());
	}
	
	@DisplayName("3. user 리스트는 페이징 되어 내려온다.")
	@Test
	void test_3() throws Exception{
		when(userService.listUsers(1, 10)).thenReturn(new RestResponsePage(user2(), admin()));
		
		String res = mockMvc.perform(get("/user/list").with(user(admin())))
							.andExpect(status().isOk())
							.andReturn().getResponse().getContentAsString(); 
		
		RestResponsePage<User> page = mapper.readValue(res, new TypeReference<RestResponsePage<User>>() {
		});
		
		assertEquals(2, page.getTotalElements());
		assertEquals(1, page.getTotalPages());
		assertEquals(0, page.getNumber());
	}
	
	@DisplayName("4. admin은 사용자에게 authority를 줄 수 있다.")
	@Test
	void test_4() throws Exception{
		//given 
		User user = User.builder()
						.id(1L)
						.email("user2@test.com")
						.name("user2")
						.roles("ROLE_USER")
						.enabled(true)
						.build();
		
		when(userService.addAuthority(user, "ROLE_ADMIN")).thenReturn(true);
		when(userService.findUser(1L)).thenReturn(Optional.of(user2 ().getUser()));
		mockMvc.perform(put(format("/user/authority/add?id=%d&authority=%s",1L,"ROLE_ADMIN"))
				.with(user(admin())))
				.andExpect(status().isOk());
				
				
		
	}
	
	@DisplayName("5. admin은 사용자에게 authority를 뺄 수 있다.")
	@Test
	void test_5() throws Exception{
		//given 
		User user = User.builder()
				.id(1L)
				.email("user2@test.com")
				.name("user2")
				.roles("ROLE_USER")
				.enabled(true)
				.build();

		when(userService.addAuthority(user, "ROLE_ADMIN")).thenReturn(true);
		when(userService.findUser(1L)).thenReturn(Optional.of(user2 ().getUser()));
		String res = mockMvc.perform(put(format("/user/authority/remove?id=%d&authority=%s",1L,"ROLE_ADMIN"))
							.with(user(admin())))
							.andExpect(status().isOk())
							.andReturn().getResponse().getContentAsString();
		
		User user1 = mapper.readValue(res, User.class);
		System.out.println(user1);
	}
}
