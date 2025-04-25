package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.CertDao;
import com.hiki.academyfinal.dto.CertDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.CertService;

@CrossOrigin
@RestController
@RequestMapping("/api/cert")
public class CertRestController {

	@Autowired
	private CertService certService;
	
	@Autowired
	private CertDao certDao;
	
	@PostMapping("/")
	public void certSend(@RequestBody CertDto certDto) {
		CertDto findDto = certDao.selectOne(certDto.getCertEmail()); 
		if(findDto == null) {
			certService.sendMail(certDto.getCertEmail());
		}
		else {
			certDao.delete(certDto.getCertEmail());
			certService.sendMail(certDto.getCertEmail());
		}
	}
	
	@PostMapping("/check")
	public void certCheck(@RequestBody CertDto certDto) {
//		System.out.println("인증 요청 들어옴: " + certDto.getCertEmail());
//		System.out.println("번호 확인 " + certDto.getCertNumber());
		CertDto qq = certDao.selectOne(certDto.getCertEmail());
//		System.out.println("find로 찾은 갖고있는번호" + qq);
		boolean result = certService.checkCert(certDto);
		if(result == false) throw new TargetNotFoundException("인증 실패");
	}
}
