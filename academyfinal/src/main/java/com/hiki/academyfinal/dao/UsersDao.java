package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.UsersDto;

@Repository
public class UsersDao {

	@Autowired 
	private SqlSession sqlSession;
	
	@Autowired
	private PasswordEncoder encoder;
	
	//회원가입 + 비밀번호 암호화
	public void insert(UsersDto usersDto) {
//		 System.out.println("usersId: " + usersDto.getUsersId()); 
		String pwEncoder = encoder.encode(usersDto.getUsersPw());
		usersDto.setUsersPw(pwEncoder);

		sqlSession.insert("users.join",usersDto);
	}
	public void insertNaver(UsersDto usersDto) {
		sqlSession.insert("users.naverJoin",usersDto);
	}
	
	//아이디중복검사 // 조회
	public UsersDto findId(String usersId) {
		return sqlSession.selectOne("users.findId", usersId);
	}
	public UsersDto findId(UsersDto usersDto) {
		return sqlSession.selectOne("users.findId", usersDto);
	}
	
	//로그인
	public UsersDto login(UsersDto usersDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(usersDto.getUsersPw());
		UsersDto findDto = findId(usersDto.getUsersId());
		System.out.println("사용자입력값 : " +usersDto);
		System.out.println("db값 : "+findDto);
		if(findDto == null) return null;
		boolean isValid = encoder.matches(usersDto.getUsersPw(), findDto.getUsersPw());
		
		System.out.println(isValid);
		return isValid ? findDto : null;
	}
	
	public UsersDto selectOne(String usersId) {
		return sqlSession.selectOne("users.find", usersId);
	}
	public UsersDto selectOne(UsersDto usersDto) {
		return sqlSession.selectOne("users.find", usersDto);
	}
	
	//이메일찾기
	public String findEmail(String usersId) {
		return sqlSession.selectOne("users.findEmail", usersId);
	}
	
	//비밀번호수정
	public boolean updatePw(Map<String, Object> usersIdPw) {
	    String usersId = (String) usersIdPw.get("usersId");
	    String rawPw = (String) usersIdPw.get("usersPw");

	    if (usersId == null || rawPw == null) {
	        throw new IllegalArgumentException("ID 또는 비밀번호가 null입니다.");
	    }

	    String pwEncoder = encoder.encode(rawPw);

	    Map<String, Object> result = new HashMap<>();
	    result.put("usersId", usersId);
	    result.put("usersPw", pwEncoder);

	    return sqlSession.update("users.updatePw", result) > 0;
	}

}
