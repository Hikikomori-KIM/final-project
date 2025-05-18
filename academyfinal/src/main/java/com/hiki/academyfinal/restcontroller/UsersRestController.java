package com.hiki.academyfinal.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.CertPwDao;
import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dao.UsersTokenDao;
import com.hiki.academyfinal.dto.CertPwDto;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.CertService;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.UsersLoginResponseVO;
import com.hiki.academyfinal.vo.UsersLoginVO;

import lombok.extern.slf4j.Slf4j;
@CrossOrigin
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UsersRestController {
	
	@Autowired
	private UsersDao usersDao;

	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsersTokenDao usersTokenDao;
	
	@Autowired
	private CertService certService;
	
	@Autowired
	private CertPwDao certPwDao;
	//가입
	@PostMapping("/")
	public void join(@RequestBody UsersDto usersDto) {
		
		System.out.println(usersDto.getUsersContact());
		usersDao.insert(usersDto);
	}
	//아이디중복
	@GetMapping("/usersId/{usersId}")
	public void overlapId(@PathVariable String usersId) {
	 UsersDto usersDto = usersDao.findId(usersId);
	 if(usersDto == null) { //null 이면 404 (가입가능)
		 throw new TargetNotFoundException();
	 	}
	}
	//로그인           //토큰 ---엑세스토큰은 집열쇠 리프레쉬는 집문서
	@PostMapping("/login")
	public UsersLoginResponseVO login(@RequestBody UsersLoginVO vo) {
		//vo를 UsersDto에맞춰서 매핑함 
		ModelMapper mapper = new ModelMapper();
		UsersDto usersDto = mapper.map(vo, UsersDto.class);
		UsersDto findDto = usersDao.login(usersDto);
		if(findDto == null) throw new TargetNotFoundException("아이디 또는 비밀번호 오류");
//		System.out.println(findDto.getUsersProvider());
		return UsersLoginResponseVO.builder()
					.usersId(findDto.getUsersId())
					.usersType(findDto.getUsersType())
					.usersProvider(findDto.getUsersProvider())
					.accessToken(tokenService.generateAccessToken(findDto))
					.refreshToken(tokenService.generateRefreshToken(findDto))
				.build();
	}
	//로그아웃
		@PostMapping("/logout")
		public void logout(@RequestHeader("Authorization") String usersToken) {
			ClaimVO claimVO = tokenService.parseBearerToken(usersToken);
			usersTokenDao.clean(claimVO.getUsersId());
		}
		
		//리프레쉬토큰 
		@PostMapping("/refresh")//헤더검사하고
		public UsersLoginResponseVO refresh(@RequestHeader("Authorization") String refreshToken) {
			//베어러토큰인지 확인하고
			ClaimVO claimVO = tokenService.parseBearerToken(refreshToken);
			//디비에있는지 확인해주고 서비스안에서 토큰알아서 제거해주게 만듬
			boolean isValid = tokenService.checkBearerToken(claimVO, refreshToken);
			if(isValid == false)
				throw new TargetNotFoundException("정보 불일치");
	
			//재발행해줌.
			return UsersLoginResponseVO.builder()
						.usersId(claimVO.getUsersId())
						.usersType(claimVO.getUsersType())
						.usersProvider(claimVO.getUsersProvider())
						.accessToken(tokenService.generateAccessToken(claimVO))
						.refreshToken(tokenService.generateRefreshToken(claimVO))
					.build();
		}
		
		//새 비밀번호 보내기 //리팩토링필요 //기능확인은 ok 기능됨 //서비스로빼야됌
		@Transactional
		@PatchMapping("/password")
		public ResponseEntity<String> password(@RequestBody UsersDto usersDto){
			System.out.println("클라이언트에서 온값 : " + usersDto);
			//id 있는지찾기 + dto가져옴
			UsersDto findDto = usersDao.findId(usersDto.getUsersId());
			if(findDto == null) throw new TargetNotFoundException("존재하지 않는 아이디");
			System.out.println("Dto 찾기 : "+ findDto);
			//그 아이디로 email 과 가져온 이메일 일치하는지 확인
			String usersEmail = usersDao.findEmail(findDto.getUsersId());
			if(usersEmail.equals(usersDto.getUsersEmail())==false) {
				throw new TargetNotFoundException("본인 이메일아님");	
			}
			//email 보내가
			CertPwDto certPwDto = new CertPwDto();
			System.out.println("usersDtoEmail :"+ usersDto.getUsersEmail());
			certPwDto.setCertPwEmail(usersEmail);
			certService.sendNewPw(certPwDto); //여기 랜덤스트링있음
			//dao저장된 문자열찾아오기
			CertPwDto certPwDto2 = certPwDao.select(usersEmail); //certPwDao 멩일이랑 randomString//pw쓸거
			
			//유저update암호화해서 저장
			Map<String,Object> usersIdPw = new HashMap<>();
			usersIdPw.put("usersId", findDto.getUsersId());
			usersIdPw.put("usersPw", certPwDto2.getCertPwString() );
			Boolean isValid = usersDao.updatePw(usersIdPw);
			if(!isValid) throw new TargetNotFoundException("비밀번호 발급 실패");
			//db정리
			certPwDao.delete(usersEmail);
			return ResponseEntity.ok("비밀번호 변경 완료");
		}
	
		@DeleteMapping("/exit")
		public void exit(@RequestBody UsersLoginVO vo) {
			System.out.println("====탈퇴로직시작====");
			System.out.println("vo"+vo);
			ModelMapper mapper = new ModelMapper();
			UsersDto usersDto = mapper.map(vo, UsersDto.class);
			System.out.println("usersDto : "+ usersDto);
			usersDao.exit(usersDto);	
			usersTokenDao.clean(vo.getUsersId());
		}
		@DeleteMapping("/naver/{usersId}")
		public void exitNaver(@PathVariable String usersId) {
			System.out.println("시발설마");
			usersTokenDao.clean(usersId);
			usersDao.exitNaver(usersId);
		}
}
