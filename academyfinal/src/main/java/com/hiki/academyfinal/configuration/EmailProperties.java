package com.hiki.academyfinal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component @Data 
@ConfigurationProperties(prefix = "custom.email") 
public class EmailProperties {
	private String username;
	private String password; 
}
