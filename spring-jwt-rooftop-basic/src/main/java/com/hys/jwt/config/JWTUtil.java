package com.hys.jwt.config;

import java.time.Instant;
import java.util.Properties;

import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hys.jwt.vo.dto.VerifyResultDto;

public class JWTUtil {
	
	public static final String AUTH_HEADER = "Authentication";
	public static final String BEARER = "Bearer ";

	private Algorithm AL;
	private long lifTime;
	
	JwtProperties jwtProperties;
	
	public JWTUtil(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.AL = Algorithm.HMAC256(jwtProperties.getSecret());
		this.lifTime = jwtProperties.getTokenLifeTime();
	}
	
	public String generate(String id) {
		return JWT.create().withSubject(id)
					.withClaim("exp", Instant.now().getEpochSecond()+lifTime)
					.sign(AL);
	}
	
	public VerifyResultDto verify(String token) {
		try {
			DecodedJWT decode = JWT.require(AL).build().verify(token);
			return VerifyResultDto.builder().id(Long.parseLong(decode.getSubject())).result(true).build();
		} catch (JWTVerificationException e) {
			DecodedJWT decode = JWT.decode(token);
			return VerifyResultDto.builder().id(Long.parseLong(decode.getSubject())).result(false).build();
		}	
	}
}
