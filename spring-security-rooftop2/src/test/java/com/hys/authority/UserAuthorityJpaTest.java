package com.hys.authority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.hys.authority.repository.UserRepository;
import com.hys.authority.service.PrincipalDetailsService;
import com.hys.authority.service.UserService;
import com.hys.authority.vo.PrincipalDetails;
import com.hys.authority.vo.User;

@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class UserAuthorityJpaTest {

	@Autowired
	private UserRepository userRepository;
	
	private UserService userService;
	
	private UserTestHelper testHelper;
	
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	void before() {
		this.userRepository.deleteAll();
		entityManager.createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1").executeUpdate();
		this.userService = new UserService(userRepository);
		this.testHelper = new UserTestHelper(userService);
		this.principalDetailsService = new PrincipalDetailsService(userRepository);
	}
	
	@DisplayName("1. 사용자를 생성한다.")
	@Test
	void test_1() {
		User user = testHelper.createUser("user1");
		List<User> userList = this.userRepository.findAll();
		
		assertEquals(1, userList.size());
		
		testHelper.assertUser(userList.get(0), "user1");
	}
	
	@DisplayName("2. 사용자의 이름을 수정한다.")
	@Test
	void test_2() {
		User user1 = testHelper.createUser("user1");
		userService.updateUsername(user1, "user2");
		
		User savedUser = userService.findUser(user1).get();
		assertEquals("user2", savedUser.getName());
	}
	
	@DisplayName("3. authority를 부여한다.")
	@Test
	void test_3() {
		User user1 = testHelper.createUser("user1", "ROLE_USER");
		userService.addAuthority(user1, "ROLE_ADMIN");
		System.out.println("user1: " + user1);
		User savedUser = userService.findUser(user1).get();
		System.out.println("savedUser: " + savedUser);
		testHelper.assertUser(savedUser, "user1", "ROLE_USER","ROLE_ADMIN");
		
	}
	
	@DisplayName("4. authority를 뺐는다.")
	@Test
	void test_4() throws Exception {
		User user1 = testHelper.createUser("admin", "ROLE_USER","ROLE_ADMIN");
		userService.removeAuthority(user1, "ROLE_USER");
		//System.out.println("user1: " + user1);
		User savedUser = userService.findUser(user1).get();
		//System.out.println("savedUser: " + savedUser);
		testHelper.assertUser(savedUser, "admin", "ROLE_ADMIN");
	}
	
	@DisplayName("5. email로 검색이 된다.")
	@Test
	void test_5() throws Exception {
		User user1 = testHelper.createUser("user1");
		PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername("user1@test.com");
		User saved = principalDetails.getUser();
		testHelper.assertUser(saved, "user1");
		
	}
	
	@DisplayName("6. role이 중복되서 추가되지 않는다.")
	@Test
	void test_6() {
		User user1 = testHelper.createUser("user1", "ROLE_USER");
		userService.addAuthority(user1, "ROLE_USER");
		userService.addAuthority(user1, "ROLE_USER");
		User savedUser = userService.findUser(user1).get();
		System.out.println("savedUser: " + savedUser);
		testHelper.assertUser(savedUser, "user1", "ROLE_USER");
	}
	
	@DisplayName("7. email이 중복되어서 들어가는가?")
	@Test
	void test_7() {
		User user1 = testHelper.createUser("user1");
		assertThrows(DataIntegrityViolationException.class, () -> {
			User user2 = testHelper.createUser("user1");
		});
	}
}
