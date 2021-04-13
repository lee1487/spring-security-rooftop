package com.hys.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "sp.jwt")
public class JwtProperties {

	private String secret = "default-secret-value";
	private long tokenLifeTime = 600;
}
