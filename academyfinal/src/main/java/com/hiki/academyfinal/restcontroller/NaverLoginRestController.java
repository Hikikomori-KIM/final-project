package com.hiki.academyfinal.restcontroller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiki.academyfinal.configuration.NaverLoginProperties;
import com.hiki.academyfinal.dao.NaverUsersDao;
import com.hiki.academyfinal.dto.NaverUsersDto;
import com.hiki.academyfinal.service.NaverLoginService;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.NaverLoginInfoVO;
import com.hiki.academyfinal.vo.NaverLoginRequestVO;
import com.hiki.academyfinal.vo.UserProfileResponseVO;
import com.hiki.academyfinal.vo.UsersLoginResponseVO;
import com.kh.spring12.error.TargetNotFoundException;

@RestController
@CrossOrigin
@RequestMapping("/api/naverLogin")
public class NaverLoginRestController {

	@Autowired
	private NaverLoginProperties naverLoginProperties;
		
	@Autowired
	private NaverLoginService naverLoginService;
	
	@Autowired
	private NaverUsersDao naverUsersDao;
	
	@Autowired
	private TokenService tokenService;
	
	
	@PostMapping("/")
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
		        System.out.println("네이버로그인결과: " + repsonse);
		        //2번 엑세스토큰 다시보내고 유저정보 받아오기 
		          try {
		        	  //엑세스토큰만 쓸거임 유저정보받아오고 우리토큰발행해줄것입니다^_^
		              ObjectMapper objectMapper = new ObjectMapper();
		              JsonNode jsonNode = objectMapper.readTree(repsonse);
		              String accessToken = jsonNode.get("access_token").asText();
	  
//		              System.out.println("엑세스토큰추출: " + accessToken);
		              UserProfileResponseVO usersProfile = naverLoginService.responseProfile(accessToken);
//		              System.out.println("유저 정보조회결과: "+usersProfile);
		              
		              NaverUsersDto naverUsersDto = new NaverUsersDto();
		              naverUsersDto.setNaverUsersId(usersProfile.getResponse().getId());
		              naverUsersDto.setNaverUsersName(usersProfile.getResponse().getName());
		              naverUsersDto.setNaverUsersEmail(usersProfile.getResponse().getEmail());
		              naverUsersDto.setNaverUsersMobile(usersProfile.getResponse().getMobile());
//		              System.out.println("유저정보 매핑결과 :"+ naverUsersDto);
		              //이미 멤버인지 확인
		              NaverUsersDto member = naverUsersDao.findNaverUser(naverUsersDto);
//		              System.out.println("이미 디비에 들어가 있는 정보인가 : "+ member);
		              if(member.equals(null) || member == null) {
		            	  //멤버 아니면 디비저장
		            	  naverUsersDao.insert(naverUsersDto);
		            	  
		            	  //다시정보뽑아놓고
		            	 NaverUsersDto findDto = naverUsersDao.findNaverUser(naverUsersDto);
//		            	 System.out.println("유저 정보추출 :"+ findDto);
		 

		            	  //후토큰발행
		            	  return UsersLoginResponseVO.builder()
		      					.usersId(findDto.getNaverUsersId())
		      					.usersType(findDto.getUsersType())
		      					.accessToken(tokenService.generateAccessTokenNaver(findDto))
		      					.refreshToken(tokenService.generateRefreshTokenNaver(findDto))
		      				.build();
		              }
		              //이미 멤버이라면
		              else {
		            	 return UsersLoginResponseVO.builder()
			      					.usersId(member.getNaverUsersId())
			      					.usersType(member.getUsersType())
			      					.accessToken(tokenService.generateAccessTokenNaver(member))
			      					.refreshToken(tokenService.generateRefreshTokenNaver(member))
			      				.build();
		            	  //그냥 토큰발행후 리턴
		            	  
		              }
		              
		          } catch (Exception e) {     
		             throw new TargetNotFoundException("로그인실패");
		          }
	}
	

	
}
