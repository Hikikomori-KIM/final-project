package com.hiki.academyfinal.restcontroller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiki.academyfinal.configuration.NaverLoginProperties;
import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.NaverLoginService;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.NaverLoginInfoVO;
import com.hiki.academyfinal.vo.NaverLoginRequestVO;
import com.hiki.academyfinal.vo.UserProfileResponseVO;
import com.hiki.academyfinal.vo.UsersLoginResponseVO;

@RestController
@CrossOrigin
@RequestMapping("/api/naverLogin")
public class NaverLoginRestController {

	@Autowired
	private NaverLoginProperties naverLoginProperties;
		
	@Autowired
	private NaverLoginService naverLoginService;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private TokenService tokenService;
	
	//얘도 리팩토링필요 ㅋㅋㅋ 여유있을때 서비스로 빼놓을게요 ^_^
	@PostMapping("/")
	@Transactional
	public UsersLoginResponseVO response(@RequestBody NaverLoginInfoVO vo) throws URISyntaxException {
		//백엔드로직
		//1. 리엑트에서 네이버한테받아온 code와 개발자 id/pw로 accesstoken요청
		//2. 다른토큰들 제외하고 accesstoken만뽑기
		//3. accesstoken네이버에게 보내서 유저정보 조회하기
		//4. db에 유저 id조회 -> 없으면  신규회원(db등록)// 정보가 이미있으면 기존회원이다.
		//5. 우리꺼 토큰발행
			//1번 네이버한테 엑세스토큰 요청하기
		   NaverLoginRequestVO naverVO = NaverLoginRequestVO.builder()
		            .grantType("authorization_code")
		            .clientId(naverLoginProperties.getClientId())
		            .clientSecret(naverLoginProperties.getClientSecret())
		            .code(vo.getCode())
		            .state(vo.getState())
		            .build();
		        String repsonse = naverLoginService.ready(naverVO);
//		        System.out.println("네이버로그인결과: " + repsonse);
		        //2번 엑세스토큰 다시보내고 유저정보 받아오기 
		          try {
		        	  //엑세스토큰만 쓸거임 유저정보받아오고 우리토큰발행해줄것입니다^_^
		              ObjectMapper objectMapper = new ObjectMapper();
		              JsonNode jsonNode = objectMapper.readTree(repsonse);
		              String accessToken = jsonNode.get("access_token").asText();
	  
//		              System.out.println("엑세스토큰추출: " + accessToken);
		              UserProfileResponseVO usersProfile = naverLoginService.responseProfile(accessToken);
//		              System.out.println("유저 정보조회결과: "+usersProfile);
		              UsersDto usersDto = new UsersDto();
		              usersDto = usersDao.findId(usersProfile.getResponse().getId());
//		              System.out.println("유저 아이디조회 : "+ usersDto);
		              if(usersDto == null) {
		            	 usersDto = new UsersDto();
		            	  //멤버 아니면 디비저장
		            	 //비밀번호 어차피 인증에 사용안됌
		            	 //하이픈들어와서 뺌 ㅡㅡ
		            	 String number = usersProfile.getResponse().getMobile();
		            	 String withoutHyphens = number.replace("-", "");
		            	 usersDto.setUsersId(usersProfile.getResponse().getId());
		            	 usersDto.setUsersName(usersProfile.getResponse().getName());
		            	 usersDto.setUsersPw("naver_dummy_pw");
		            	 usersDto.setUsersContact(withoutHyphens);
		            	 usersDto.setUsersEmail(usersProfile.getResponse().getEmail());
		            	 usersDto.setUsersProvider("naver");
//		            	 System.out.println("현재 provider 값: " + usersDto.getUsersProvider()); 
		            	 usersDao.insertNaver(usersDto);
//		            	 System.out.println("인서트후 provider 값: " + usersDto.getUsersProvider()); 
		            	 UsersDto findDto = usersDao.findId(usersDto);
		            
		            	  //후토큰발행
		            	  return UsersLoginResponseVO.builder()
		      					.usersId(findDto.getUsersId())
		      					.usersType(findDto.getUsersType())
		      					.accessToken(tokenService.generateAccessToken(findDto))
		      					.refreshToken(tokenService.generateRefreshToken(findDto))
		      				.build();
		              }
		              //이미 멤버이라면
		              else {
		            	 return UsersLoginResponseVO.builder()
			      					.usersId(usersDto.getUsersId())
			      					.usersType(usersDto.getUsersType())
			      					.accessToken(tokenService.generateAccessToken(usersDto))
			      					.refreshToken(tokenService.generateRefreshToken(usersDto))
			      				.build();
		            	 //그냥 토큰발행후 리턴
		            	  
		              }
		              
		          } catch (Exception e) {     
		             throw new TargetNotFoundException("로그인실패"+e.getMessage());
		          }
	}
	

	
}
