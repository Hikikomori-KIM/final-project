package com.hiki.academyfinal.configuration;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "custom.token")
public class TokenProperties {
	private String issuer;	//나
	private String secretKey; //비밀번호 프로퍼티참고
	private int accessLimit;  
	private int refreshLimit; 
	private int renewalLimit; 
	
	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
}
