package com.hys.module.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hys.module.repository.UserRepository;
import com.hys.module.vo.PrincipalDetails;
import com.hys.module.vo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username));
		
		return new PrincipalDetails(userEntity);
	}

	
}
