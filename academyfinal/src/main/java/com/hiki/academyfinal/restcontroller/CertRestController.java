package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dto.CertDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.CertService;

@CrossOrigin
@RestController
@RequestMapping("/api/cert")
public class CertRestController {

	@Autowired
	private CertService certService;
	
	@PostMapping("/")
	public void certSend(@RequestBody CertDto certDto) {
		certService.sendMail(certDto.getCertEmail());
		//이메일주고 끝
	}
	
	@PostMapping("/check")
	public void certCheck(@RequestBody CertDto certDto) {
		if(certService.checkCert(certDto) == false) {
			throw new TargetNotFoundException("인증 실패"); //실패하면 404..?;뭐줘
		}
	}
}
