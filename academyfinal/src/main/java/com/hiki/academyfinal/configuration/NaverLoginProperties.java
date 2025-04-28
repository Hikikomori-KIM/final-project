package com.hiki.academyfinal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "custom.naver")
public class NaverLoginProperties {
	private String clientId;
	private String clientSecret;
}
