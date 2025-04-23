package com.hiki.academyfinal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component @Data
@ConfigurationProperties(prefix = "custom.cert") 
public class CertProperties {
	private int expireMinutes;//custom.cert.expire-minutes
	private int expireAccept;//custom.cert.expire-accept
}
