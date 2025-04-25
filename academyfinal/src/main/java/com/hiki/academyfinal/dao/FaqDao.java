package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.FaqDto;

@Repository
public class FaqDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<FaqDto> selectList() {
		return sqlSession.selectList("faq.list");	
	}
	
	public FaqDto selectOne(long faqNo) {
		return sqlSession.selectOne("faq.find", faqNo);
	}
	
	public FaqDto selectOne(FaqDto faqDto) {
		return sqlSession.selectOne("faq.find", faqDto);
	}
	
}
