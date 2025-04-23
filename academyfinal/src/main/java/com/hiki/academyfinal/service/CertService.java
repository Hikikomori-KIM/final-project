package com.hiki.academyfinal.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hiki.academyfinal.configuration.CertProperties;
import com.hiki.academyfinal.dao.CertDao;
import com.hiki.academyfinal.dto.CertDto;
import com.hiki.academyfinal.util.RandomGenerator;

@Service
public class CertService {

	@Autowired
	private RandomGenerator randomGenerator;
	
	@Autowired
	private CertDao certDao;
	
	@Autowired
	private JavaMailSender sender; //zz통째로불러오는게 아니라 기능을좀ㅋㅋ
	
	@Autowired
	private CertProperties certProperties;
	
	public void sendMail(String usersEmail) {
		String number = randomGenerator.randomNumber(8);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(usersEmail);
		message.setSubject("이메일 인증번호입니다");
		message.setText("인증번호[ "+number+" ]를 입력해주세요");
		
		sender.send(message);
		certDao.sendOrUpdate(CertDto.builder()
				.certEmail(usersEmail)
				.certNumber(number)
				.build());
		
	}
	//시간차구하기 + 인증번호 꺼내서 equals확인
	public boolean checkCert(CertDto certDto) {
		CertDto findDto = certDao.selectOne(certDto);
		if(findDto == null) return false;
		
		//boolean c1 = DB인증번호와 제출한번호가 같을것;
		//boolean c2 = 제한시간을 넘기지 않을 것;
		
		boolean c1 = findDto.getCertNumber().equals(certDto.getCertNumber());
		
		LocalDateTime t1 = findDto.getCertTime().toLocalDateTime();//발송시각
		LocalDateTime t2 = LocalDateTime.now();
		Duration duration = Duration.between(t1, t2);//차이 계산
		boolean c2 = duration.toMinutes() < 10;
		boolean isValid = c1 && c2;
		
		if(isValid) {
			certDao.confirm(certDto.getCertEmail());//인증시간 기록
			return true;
		}
		
		return false;//인증 실패
	}

//	// 정기적으로 인증정보 중 사용할 수 없는 것들을 제거하는 메소드
//	@Scheduled(cron = "0 0 * * * *")
//	public void work() {
//		certDao.clean(certProperties.getExpireMinutes(), certProperties.getExpireAccept());
//	}
}
