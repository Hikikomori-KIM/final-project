package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CertDto;

@Repository
public class CertDao {

	@Autowired
	private SqlSession sqlSession;
	
	//인증메일첨부
	public void send(CertDto certDto) {
		Map<String,Object> result = new HashMap<>();
		result.put("certEmail", certDto.getCertEmail());  
		result.put("certNumber", certDto.getCertNumber()); 
		sqlSession.insert("cert.send", result);
	}
	
	//인증메일삭제
	public boolean delete(String certEmail) {
		Map<String,String> result = new HashMap<>();
		result.put("certEmail", certEmail);
		return sqlSession.delete("cert.delete",result)>0;
	}
	
	//인증메일찾기 유저이멜로
	public CertDto selectOne(String certEmail) {
		Map<String,String> result = new HashMap<>();
		result.put("certEmail", certEmail);
		return sqlSession.selectOne("cert.find",result);
	}
	//오버로딩 
	public CertDto selectOne(CertDto certDto) {
		Map<String,Object> result = new HashMap<>();
		result.put("certDto", certDto.getCertEmail());
		return sqlSession.selectOne("cert.find",result);
	}
	
	
	//메일 다시전송
	public boolean update(CertDto certDto) {
		CertDto findDto = selectOne(certDto.getCertEmail());
		Map<String,Object> result = new HashMap<>();
		result.put("certEmail", findDto.getCertEmail());
		result.put("certNumber", findDto.getCertNumber());
		return sqlSession.update("cert.update",result)>0;
	}
	
	//메일이 이미 보내졌으면 업데이트 아니면 그냥 보냄
	public void sendOrUpdate(CertDto certDto) {
		if(selectOne(certDto) == null) {
			send(certDto); 
		}
		else {
			update(certDto);
		}
	}
	public boolean confirm(String certEmail) {
		Map<String,String> result = new HashMap<>();
		result.put("certEmail", certEmail);
		return sqlSession.delete("cert.confirm", result) > 0;
	}
	
	public boolean clean(int expireMinutes, int expireAccept) {
		Map<String,Object> result = new HashMap<>();
		result.put("expireMinutes", expireMinutes);
		result.put("expireAccept", expireAccept);
		return sqlSession.delete("cert.clean",result)>0;
	}
}
