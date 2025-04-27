package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.UsersTokenDto;

@Repository
public class UsersTokenDao {
	@Autowired
	private SqlSession sqlSession;
	
	public UsersTokenDto insert(UsersTokenDto usersTokenDto) {
		long usersTokenNo = sqlSession.selectOne("usersToken.sequence");
		usersTokenDto.setUsersTokenNo(usersTokenNo);
		sqlSession.insert("usersToken.insert",usersTokenDto);
		return sqlSession.selectOne("usersToken.find",usersTokenDto);
	}
	
	public UsersTokenDto findByTargetAndToken(UsersTokenDto usersTokenDto) {
		return sqlSession.selectOne("usersToken.findByTargetAndToken",usersTokenDto);
	}
	
	public boolean delete(UsersTokenDto usersTokenDto) {
		return sqlSession.delete("usersToken.delete",usersTokenDto)>0;
	}
	
	public boolean clean(String usersTokenTarget) {
		return sqlSession.delete("usersToken.deleteByUsersTokenTarget", usersTokenTarget)>0;
	}
	
	public boolean clean(UsersTokenDto usersTokenDto) {
		return sqlSession.delete("usersToken.deleteByUsersTokenTarget",usersTokenDto) >0;
	}
}
