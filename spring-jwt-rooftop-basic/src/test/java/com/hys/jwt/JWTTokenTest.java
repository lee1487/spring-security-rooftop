package com.hys.jwt;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTTokenTest {
	
	static void printClaims(String key, Claim value) {
		if (value.isNull()) {
			System.out.printf("%s:%s\n", key, "none");
			return;
		}
		
		if (value.asString() != null) {
			System.out.printf("%s:{str}%s\n", key, value.asString());
			return;
		}
		
		if (value.asLong() != null) {
			System.out.printf("%s:{lng}%d\n", key, value.asLong());
			return;
		}
		
		if (value.asInt() != null) {
			System.out.printf("%s:{int}%d\n", key, value.asInt());
			return;
		}
		
		if (value.asBoolean() != null) {
			System.out.printf("%s:{bol}%b\n", key, value.asBoolean());
			return;
		}
		
		if (value.asDate() != null) {
			System.out.printf("%s:{dte}%s\n", key, value.asDate().toString());
			return;
		}
		
		if (value.asDouble() != null) {
			System.out.printf("%s:{dbl}%f\n", key, value.asDouble());
			return;
		}
		
		String[] values = value.asArray(String.class);
		if (values != null) {
			System.out.printf("%s:{arr}%s\n",key, Stream.of(values).collect(Collectors.toList()));
			return;
		}
		
		Map valueMap = value.asMap();
		if (valueMap != null) {
			System.out.printf("%s:{map}%s\n",key, valueMap);
			return;
		}
		
		System.out.println("=====> unknown type for :"+ key);
		
	}

	@DisplayName("1. JWT 토큰이 잘 만들어 진다.")
	@Test
	void test_() throws InterruptedException {
		Algorithm AL = Algorithm.HMAC256("hello"); 
		String token = JWT.create()
				.withSubject("hyeonse")
				.withClaim("exp", Instant.now().getEpochSecond()+3)
				.withArrayClaim("role", new String[] {"ROLE_ADMIN", "ROLE_USER"})
				.sign(AL);
					
		System.out.println(token);
		
		Thread.sleep(1000);
		
		//DecodedJWT decode = JWT.decode(token);
		DecodedJWT decode = JWT.require(AL).build().verify(token);
		
		printClaims("typ", decode.getHeaderClaim("typ"));
		printClaims("alg", decode.getHeaderClaim("alg"));
		System.out.println("=================");
		decode.getClaims().forEach(JWTTokenTest::printClaims);
		
		Thread.sleep(2000);
		
		assertThrows(TokenExpiredException.class, () -> {
			JWT.require(AL).build().verify(token);
		});
		
		
	}
}
