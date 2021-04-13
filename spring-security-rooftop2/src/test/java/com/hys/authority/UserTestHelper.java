package com.hys.authority;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.hys.authority.service.UserService;
import com.hys.authority.vo.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserTestHelper {

	private final UserService userService;
	
	public User createUser(String name) {
		User user = User.builder()
					.name(name)
					.email(name+"@test.com")
					.password(name+"1234")
					.enabled(true)
					.build();
		//PrincipalDetails principalDetails = new PrincipalDetails(user);
		
		return userService.save(user);
			
	}
	
	public User createUser(String name, String...  authorities) {
		User user = createUser(name);
		Stream.of(authorities).forEach(auth -> userService.addAuthority(user, auth));
		
		return user;
	}
	
	public void assertUser(User user, String name) {
		assertNotNull(user.getId());
		assertNotNull(user.getCreated());
		assertTrue(user.isEnabled());
		assertEquals(name, user.getName());
		assertEquals(name+"@test.com", user.getEmail());
		assertEquals(name+"1234", user.getPassword());
		
		
	}
	
	public void assertUser(User user, String name, String... authorities) {
		assertUser(user,name);
		assertTrue(user.getRoleList().containsAll(
				Stream.of(authorities).map(auth -> new String(auth)).collect(Collectors.toList())
		));
	}
}
