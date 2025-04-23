package com.hiki.academyfinal;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

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
//System.out.println("단문 메세지 발송 테스트 시작");
//		
//		//[1]
//		JavaMailSenderImpl sender = new JavaMailSenderImpl();
//		
//		//[2] 
//		sender.setHost("smtp.gmail.com");//이용할 업체의 별칭 또는 IP(위치 정보)
//		sender.setPort(587);//이용할 업체의 서비스 실행 번호
//		sender.setUsername("sswoo4111");//이용할 업체의 사용자 계정이름
//		sender.setPassword("utiajjaznuxvgena");//이용할 업체의 사용자 계정 비밀번호(구글은 앱 비밀번호)
//		
//		Properties props = new Properties();//추가 정보 저장소 생성
//		props.setProperty("mail.smtp.auth", "true");//인증 후 이용 설정
//		props.setProperty("mail.smtp.debmg", "true");//디버깅 허용 설정
//		props.setProperty("mail.smtp.starttls.enable", "true");//TLS 사용 설정
//		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");//TLS 버전 설정
//		props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");//신뢰할 수 있는 주소로 등록
//		sender.setJavaMailProperties(props);//추가 정보 설정
//		
//		//[3]
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo("sswoo4111@gmail.com");//수신인(여러개 가능)
//		//message.setCc(null);//참조인(여러개 가능)
//		//message.setBcc(null);//숨은 참조인(여러개 가능)
//		message.setSubject("테스트 이메일");//제목
//		message.setText("Hello!");//내용
//		
//		//[4]
//		sender.send(message);
//		
//		System.out.println("단문 메세지 발송 테스트 종료");
//	}

	}
}
