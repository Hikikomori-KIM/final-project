package com.hiki.academyfinal.restcontroller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dao.UsersTokenDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.UsersLoginResponseVO;
import com.hiki.academyfinal.vo.UsersLoginVO;
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersRestController {
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private UsersLoginResponseVO usersLoginResponseVO;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsersTokenDao usersTokenDao;
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
		System.out.println("입력된 비밀번호: " + usersDto.getUsersPw());
		UsersDto findDto = usersDao.login(usersDto);
		if(findDto == null) throw new TargetNotFoundException("아이디 또는 비밀번호 오류");
		
		return UsersLoginResponseVO.builder()
					.usersId(findDto.getUsersId())
					.usersType(findDto.getUsersType())
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
						.accessToken(tokenService.generateAccessToken(claimVO))
						.refreshToken(tokenService.generateRefreshToken(claimVO))
					.build();
		}
}
