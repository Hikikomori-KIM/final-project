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
	String pwEncoder = encoder.encode(usersDto.getUsersPw());
	usersDto.setUsersPw(pwEncoder);
	Map<String,Object> result = new HashMap<>();
	result.put("usersDto", usersDto);
	sqlSession.insert("users.join",usersDto);
	}
}
