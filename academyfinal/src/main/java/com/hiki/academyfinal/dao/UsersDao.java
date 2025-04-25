package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.UsersDto;

@Repository
public class UsersDao {

	@Autowired 
	private SqlSession sqlSession;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	//회원가입 + 비밀번호 암호화
	public void insert(UsersDto usersDto) {
		 System.out.println("usersId: " + usersDto.getUsersId()); 
		String pwEncoder = encoder.encode(usersDto.getUsersPw());
		usersDto.setUsersPw(pwEncoder);
//		Map<String,Object> result = new HashMap<>();
//		result.put("usersDto", usersDto); //어이가없네 이거 맵으로하면 데이터 랜덤으로들어감ㅋ
		sqlSession.insert("users.join",usersDto);
	}
	
	//아이디중복검사 // 조회
	public UsersDto findId(String usersId) {
		Map<String,Object> result = new HashMap<>();
		result.put("usersId",usersId);
		return sqlSession.selectOne("users.findId", result);
	}
	
	
}
