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
		System.out.println(findDto.toString());
	}
	
	@PostMapping("/check")
	public void certCheck(@RequestBody CertDto certDto) {
		if(certService.checkCert(certDto) == false) {
			throw new TargetNotFoundException("인증 실패"); //실패하면 404..?;뭐줘
		}
	}
}
