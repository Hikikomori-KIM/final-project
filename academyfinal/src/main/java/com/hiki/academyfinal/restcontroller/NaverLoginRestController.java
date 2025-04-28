package com.hiki.academyfinal.restcontroller;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.configuration.NaverLoginProperties;
import com.hiki.academyfinal.service.NaverLoginService;
import com.hiki.academyfinal.vo.NaverLoginInfoVO;
import com.hiki.academyfinal.vo.NaverLoginRequestVO;
import com.hiki.academyfinal.vo.UserProfileResponseVO;
import com.hiki.academyfinal.vo.UsersLoginResponseVO;
import com.kh.spring12.vo.kakaopay.KakaoPayApproveVO;

@RestController
@CrossOrigin
@RequestMapping("/api/naverLogin")
public class NaverLoginRestController {

	@Autowired
	private NaverLoginProperties naverLoginProperties;
		
	@Autowired
	private NaverLoginService naverLoginService;
	
	private Map<String, KakaoPayApproveVO> flashMap = 
			Collections.synchronizedMap(new HashMap<>());
	//엑세스토큰요청
	@PostMapping("/accessRequest")
	public String response(@RequestBody NaverLoginInfoVO vo) throws URISyntaxException {
		
		NaverLoginRequestVO naverVO = NaverLoginRequestVO.builder()
				.grantType("authorization_code")
				.clientId(naverLoginProperties.getClientId())
				.clientSecret(naverLoginProperties.getClientSecret())
				.code(vo.getCode())
				.state(vo.getState())
				.build();
		  String result = naverLoginService.ready(naverVO);
		  System.out.println("Naver Login Response: " + result);
		  return result;
	}
	
	//유저정보 받아오기 + 토큰뿌리기
	@GetMapping("/token/{accessToken}")
	public UserProfileResponseVO login(@PathVariable String accessToken) throws URISyntaxException {
		UserProfileResponseVO result = naverLoginService.responseProfile(accessToken);
		System.out.println(result);
		
		return result;
	}
	
	
}
