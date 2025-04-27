package com.hiki.academyfinal.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiki.academyfinal.configuration.TokenProperties;
import com.hiki.academyfinal.dao.UsersTokenDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.dto.UsersTokenDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.vo.ClaimVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService {
	
	@Autowired
	private TokenProperties tokenProperties;
	
	@Autowired
	private UsersTokenDao usersTokenDao;
	//새토큰생성
	public String generateAccessToken(UsersDto usersDto) {
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		c.add(Calendar.MINUTE, tokenProperties.getAccessLimit());
		Date limit = c.getTime();
		//제이슨웹토큰서비스임
		return Jwts.builder()
					.signWith(tokenProperties.getKey())
					.expiration(limit)
					.issuer(tokenProperties.getIssuer())
					.issuedAt(now)
					.claim("usersId", usersDto.getUsersId())
					.claim("usersType", usersDto.getUsersType())
				.compact();
	}
		//토큰해석
	public ClaimVO parse(String token) {
		Claims claims = (Claims) Jwts.parser()
				.verifyWith(tokenProperties.getKey())
				.requireIssuer(tokenProperties.getIssuer())
				.build()
				.parse(token)
				.getPayload();
		
		return ClaimVO.builder()
				.usersId((String) claims.get("usersId"))
				.usersType((String) claims.get("usersType"))
				.build();
	}
	
	//리프레쉬토큰생성
	public String generateRefreshToken(UsersDto usersDto) {
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		c.add(Calendar.MINUTE, tokenProperties.getRefreshLimit());
		Date limit = c.getTime();

		//토큰생성
		String tokenValue = Jwts.builder()
			.signWith(tokenProperties.getKey())
			.expiration(limit)
			.issuer(tokenProperties.getIssuer())
			.issuedAt(now)
			.claim("usersId", usersDto.getUsersId())
			.claim("usersType",usersDto.getUsersType())
		.compact();
		
		//db저장
		UsersTokenDto	usersTokenDto = usersTokenDao.insert(
			UsersTokenDto.builder()
				.usersTokenTarget(usersDto.getUsersId())
				.usersTokenValue(tokenValue)
			.build()
		);
		return tokenValue;
	}

	//베얼어인지 토큰검사 
	public ClaimVO parseBearerToken(String bearerToken) {
		if(bearerToken == null)
			throw new TargetNotFoundException("토큰이 없습니다");
		if(bearerToken.startsWith("Bearer ") == false)
			throw new TargetNotFoundException("Bearer토큰이 아닙니다");
		
		String token = bearerToken.substring(7);
		return parse(token);
	}

	//Bearer뒤에 db에있는지검사 
	public boolean checkBearerToken(ClaimVO claimVO, String bearerToken) {
		String token = bearerToken.substring(7);
		UsersTokenDto usersTokenDto = 
				usersTokenDao.findByTargetAndToken(
						UsersTokenDto.builder()
							.usersTokenTarget(claimVO.getUsersId())
							.usersTokenValue(token)
						.build()
				);
		//이씅면 지워줌 재발행위해 여기서먼저 ㅇㅇ
		if(usersTokenDto != null) {
			usersTokenDao.delete(usersTokenDto);
			return true;
		}
		return false;
	}

	public String generateAccessToken(ClaimVO claimVO) {
		return generateAccessToken(UsersDto.builder()
					.usersId(claimVO.getUsersId())
					.usersType(claimVO.getUsersType())
				.build());
	}
	public String generateRefreshToken(ClaimVO claimVO) {
		return generateRefreshToken(UsersDto.builder()
					.usersId(claimVO.getUsersId())
					.usersType(claimVO.getUsersType())
				.build());
	}

	//토큰시간계산
	public long getRemainTime(String bearerToken) {
		String token = bearerToken.substring(7);
		
		//토큰 해석
		Claims claims = (Claims) Jwts.parser()
				.verifyWith(tokenProperties.getKey())
				.requireIssuer(tokenProperties.getIssuer())
			.build()
				.parse(token)
				.getPayload();
		

		Date expire = claims.getExpiration();
		Date now = new Date();
		
		return expire.getTime() - now.getTime();
	}
}
