package com.hiki.academyfinal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class changePwTest {
	@Autowired
	private PasswordEncoder encoder;
	@Test
	public void change() {
		String aaa = "_*L$mrZ,C@";
		String aaa222 = encoder.encode(aaa);
		log.debug(aaa222);
		boolean aaa333 = encoder.matches(aaa, aaa222);
		
		log.debug("결과={}",aaa333);
	}
	
}
