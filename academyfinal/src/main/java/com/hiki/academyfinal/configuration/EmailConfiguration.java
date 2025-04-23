package com.hiki.academyfinal.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

	@Autowired
	private EmailProperties siwooMail;
	JavaMailSenderImpl sender = new JavaMailSenderImpl();

//	얘는 host , port , username, password  4가지 설정을 해줘야함
//	port = 서비스 실행번호  = 587
//	username= 사용자 계정이름  = sswoo4111 맞나?;
//	password = 사용자계정 비밀번호 = 메일비번받은거 
	@Bean
	public JavaMailSenderImpl sender() {
		sender.setHost("mail.smtp.auth");
		sender.setPort(587);
		sender.setUsername(siwooMail.getUsername());
		sender.setPassword(siwooMail.getPassword());
	Properties props = new Properties();  
		props.setProperty("mail.smtp.auth", "true"); //인증후이용
		props.setProperty("mail.smtp.debmg", "true"); // 디버깅허용
		props.setProperty("mail.smtp.starttls.enable", "true"); // TLS사용
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2"); // 버전
		props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com"); //신뢰 
		sender.setJavaMailProperties(props); //저장
		return sender;
	}
	

}
