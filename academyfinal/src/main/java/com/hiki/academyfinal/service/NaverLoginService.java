package com.hiki.academyfinal.service;

import java.net.URI;
import java.net.URISyntaxException;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;


import com.hiki.academyfinal.vo.NaverLoginRequestVO;
import com.hiki.academyfinal.vo.UserProfileResponseVO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class NaverLoginService {



	
	//엑세스토큰 네이버꺼 받아오기
	public String ready(NaverLoginRequestVO vo) throws URISyntaxException {
		URI uri = new URI ("https://nid.naver.com/oauth2.0/token?grant_type="+vo.getGrantType() 
							+"&client_id="+vo.getClientId()
							+"&client_secret="+vo.getClientSecret()
							+ "&code="+vo.getCode()
							+ "&state="+vo.getState());
		 RestTemplate restTemplate = new RestTemplate();

	     ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, null, String.class);
	     return response.getBody();
		 
	}
	
	//유저정보받아오기
	public UserProfileResponseVO responseProfile(@PathVariable String accessToken) throws URISyntaxException {
	    URI uri = new URI("https://openapi.naver.com/v1/nid/me");

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Bearer " + accessToken);

	    //헤더 포함
	    HttpEntity<Void> entity = new HttpEntity<>(headers);

	    RestTemplate restTemplate = new RestTemplate();
	    
	    ResponseEntity<UserProfileResponseVO> response = restTemplate.exchange(
	        uri,
	        HttpMethod.GET,
	        entity,
	        UserProfileResponseVO.class
	    );
	    return response.getBody();
	}

	
	
	
}
