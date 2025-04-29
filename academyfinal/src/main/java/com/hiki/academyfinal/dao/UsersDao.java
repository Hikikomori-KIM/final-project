package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
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
		 System.out.println("usersId: " + usersDto.getUsersId()); 
		String pwEncoder = encoder.encode(usersDto.getUsersPw());
		usersDto.setUsersPw(pwEncoder);

		sqlSession.insert("users.join",usersDto);
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
		UsersDto findDto = findId(usersDto);
		if(findDto == null) return null;
		
		boolean isValid = encoder.matches(usersDto.getUsersPw(), findDto.getUsersPw());
		return isValid ? findDto : null;
	}
	
	public UsersDto selectOne(String usersId) {
		return sqlSession.selectOne("users.find", usersId);
	}
	public UsersDto selectOne(UsersDto usersDto) {
		return sqlSession.selectOne("users.find", usersDto);
	}
	
}
