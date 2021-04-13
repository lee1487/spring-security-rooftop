package com.hys.module.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hys.module.repository.UserRepository;
import com.hys.module.vo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	@Transactional
	public User save(User user) throws DataIntegrityViolationException{
//		String rawPassword = user.getPassword();
//		String encPassword = passwordEncoder.encode(rawPassword);
//		user.setPassword(encPassword);
		return userRepository.save(user);
	}
	
	@Transactional
	public Optional<User> findUser(Long id) {
		return userRepository.findById(id);
	}
	
	@Transactional
	public boolean updateUsername(User user, String username) {
		User entity = userRepository.findById(user.getId())
				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + user.getEmail()));
		
		entity.setName(username);
		return true;
	}
	
	@Transactional
	public boolean addAuthority(Long id, String authority) {
		User entity = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
		
		List<String> list = new ArrayList<>();
		list.addAll(entity.getRoleList());
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
		
		
		
		entity.setRoles(auth);
		
		return result;
		
	}
	
	@Transactional
	public boolean removeAuthority(Long id, String authority) throws Exception {
		User entity = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
		
		List<String> list = new ArrayList<>();
		list.addAll(entity.getRoleList());
		boolean result = false;
		//list = user.getRoleList();
		if (list.indexOf(authority) < 0) {
			throw new Exception("해당 권한이 존재하지 않아 삭제할 수 없습니다.");
		} else {
			list.remove(authority);
			String auth = StringUtils.join(list, ",");
			result = true;
			
			
			
			entity.setRoles(auth);
		}
		
		return result;
		
		
	}
	
	
}
