package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CertPwDto;

@Repository
public class CertPwDao {

	@Autowired
	private SqlSession sqlSession;
	public void insert(CertPwDto certPwDto) {
		Map<String,Object> result = new HashMap<>();
		result.put("certPwEmail", certPwDto.getCertPwEmail());
		result.put("certPwString", certPwDto.getCertPwString());
		sqlSession.insert("certPw.add",result);
	}
	
	public boolean delete(String certPwEmail) {
		return sqlSession.delete("certPw.delete",certPwEmail)>0;
	}
	
	public CertPwDto select(String certPwEmail) {
		return sqlSession.selectOne("certPw.findString", certPwEmail);
	}
}
