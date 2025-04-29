package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.NaverUsersDto;

@Repository
public class NaverUsersDao {

	@Autowired
	private SqlSession sqlSession;
	
	//호원가입+로그인
	public void insert(NaverUsersDto naverUsersDto) {
		sqlSession.insert("naverUsers.join",naverUsersDto);
	}
	//유저찾기
	public NaverUsersDto findNaverUser(NaverUsersDto naverUsersDto) {
		return sqlSession.selectOne("naverUsers.find",naverUsersDto);
	}
}
