package com.hys.authority.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails{
	
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoleList().forEach(r -> {
			authorities.add(() -> r); // 자바스크립트 () -> r
		});
		return authorities;
	}
	

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}
}
