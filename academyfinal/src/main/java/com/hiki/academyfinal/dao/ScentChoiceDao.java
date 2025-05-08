package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ScentChoiceDto;

@Repository
public class ScentChoiceDao {

	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("scentChoice.sequence");
	}
	
	public void insert(ScentChoiceDto scentChoiceDto) {
		int sequence = sequence();
		scentChoiceDto.setScentChoiceNo(sequence);
		sqlSession.insert("scentChoice.add", scentChoiceDto);
	}
	
	//한번에 인서트처리
	public void insertAll(List<ScentChoiceDto> scentChoiceDtoList) {
		for(ScentChoiceDto scentChoiceDto : scentChoiceDtoList) {
			int sequence =sequence();
			scentChoiceDto.setScentChoiceNo(sequence);
		}
		sqlSession.insert("scentChoice.addAll",scentChoiceDtoList);
	}
}
