package com.hys.authority.vo;

import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {
	private String message;
	@JsonIgnore
	private Authentication auth;
}
