package com.hiki.academyfinal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hiki.academyfinal.configuration.EmailConfiguration;
import com.hiki.academyfinal.configuration.EmailProperties;
import com.hiki.academyfinal.dao.CertDao;
import com.hiki.academyfinal.service.CertService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class mailTest {

	@Autowired
	private CertService certService;
	@Autowired
	private CertDao certDao;
	@Autowired
	private EmailProperties emailProperties;
	@Autowired
	private EmailProperties siwooMail;
	@Test
	public void email() {
		String usersEmail = "oo54941@gamil.com";
		certService.sendMail(usersEmail);
		log.debug("userEmail={}",usersEmail);
		log.debug("siwooMail={}",siwooMail.getUsername());
		log.debug("siwooMail={}",siwooMail.getPassword());

	}
}
