package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ScentRecommendationDto;

@Repository
public class ScentRecommendationDao {

	@Autowired
	private SqlSession sqlSession;
	public int sequence() {
		return sqlSession.selectOne("scentRecommendation.sequence");
	}
	public void insert(ScentRecommendationDto scentRecommendationDto) {
		int sequence = sequence();
		scentRecommendationDto.setScentRecommendationNo(sequence);
		sqlSession.insert("scentRecommendation.add",scentRecommendationDto);
	}
}
