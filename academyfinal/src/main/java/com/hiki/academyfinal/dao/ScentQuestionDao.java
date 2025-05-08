package com.hiki.academyfinal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ScentQuestionDto;

@Repository
public class ScentQuestionDao {

	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("scentQuestion.sequence");
	}
	//카운트 받아서 배열로쳐버리기 
	public List<Integer>sequence (int count){
		List<Integer>sequenceList = new ArrayList<>();
		for(int i=0; i<count; i++) {
			int sequence = sqlSession.selectOne("scentQuestion.sequence");
			sequenceList.add(sequence);
		}
		return sequenceList;
	}
	
	public void insert(ScentQuestionDto scentQuestionDto) {
		int sequence = sequence();
		scentQuestionDto.setScentQuestionNo(sequence);
		sqlSession.insert("scentQuestion.add",scentQuestionDto);
	}
}
