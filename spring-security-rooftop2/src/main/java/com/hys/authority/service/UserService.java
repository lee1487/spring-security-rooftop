package com.hys.authority.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hys.authority.repository.UserRepository;
import com.hys.authority.vo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public User save(User user) throws DataIntegrityViolationException{
//		String rawPassword = user.getPassword();
//		String encPassword = passwordEncoder.encode(rawPassword);
//		user.setPassword(encPassword);
		return userRepository.save(user);
	}
	
	@Transactional
	public Optional<User> findUser(User user) {
		return userRepository.findById(user.getId());
	}
	
	@Transactional
	public Optional<User> findUser(Long id) {
		return userRepository.findById(id);
	}
	
	@Transactional
	public void updateUsername(User user, String username) {
		User entity = userRepository.findById(user.getId())
				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + user.getEmail()));
		
		entity.setName(username);
	}
	
	@Transactional
	public boolean addAuthority(User user, String authority) {
		List<String> list = new ArrayList<>();
		list.addAll(user.getRoleList());
		//list = user.getRoleList();
		boolean overlap = false;
		boolean result = false;
		
		for (String auth: list) {
			if (auth.equals(authority)) {
				overlap = true;
			}
		}
		if (overlap == false) {
			list.add(authority);
			result = true;
		}
		
		String auth = StringUtils.join(list, ",");
		
		User entity = userRepository.findById(user.getId())
				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + user.getEmail()));
		
		entity.setRoles(auth);
		
		return result;
		
	}
	
	@Transactional
	public void removeAuthority(User user, String authority) throws Exception {
		List<String> list = new ArrayList<>();
		list.addAll(user.getRoleList());
		//list = user.getRoleList();
		if (list.indexOf(authority) < 0) {
			throw new Exception("해당 권한이 존재하지 않아 삭제할 수 없습니다.");
		} else {
			list.remove(authority);
			String auth = StringUtils.join(list, ",");
			
			User entity = userRepository.findById(user.getId())
					.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + user.getEmail()));
			
			entity.setRoles(auth);
		}
		
		
	}

	@Transactional
	public Page<User> listUsers(Integer page, Integer size) {
		return userRepository.findAll(PageRequest.of(page-1, size));
	}

	
	
}
