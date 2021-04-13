package com.hys.blog.core.service;

import com.hys.blog.core.entity.Member;
import com.hys.blog.core.security.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {

	private String id;
	private String userName;
	private String email;
	private Role role;
	
	public static MemberDTO of(Member member) {
		return MemberDTO.builder()
                .id(String.valueOf(member.getId()))
                .userName(member.getUsername())
                .email(member.getEmail())
                .role(Role.of(member.getRole()))
                .build();
	}
}
