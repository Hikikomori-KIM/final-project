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
	//카운트 받아서 배열로쳐버리기 이딴거쓰면안됌 시퀀스어레이로 xx
//	public List<Integer>sequence (int count){
//		List<Integer>sequenceList = new ArrayList<>();
//		for(int i=0; i<count; i++) {
//			int sequence = sqlSession.selectOne("scentQuestion.sequence");
//			sequenceList.add(sequence);
//		}
//		return sequenceList;
//	}
	//여기 시퀀스 또주니까 그냥시쿠너스삭제 
	public void insert(ScentQuestionDto scentQuestionDto) {
		sqlSession.insert("scentQuestion.add",scentQuestionDto);
	}
	public List<Integer> sequenceList(int size){
		return sqlSession.selectList("scentQuestion.sequenceList",size);
	}
	
	public boolean updateQuestion(ScentQuestionDto scentQuestionDto) {
		return sqlSession.update("scentQuestion.update",scentQuestionDto) >0;
	}
	
	public boolean deleteQuestion(int scentQuestionNo) {
		return sqlSession.delete("scentQuestion.delete",scentQuestionNo)>0;
	}
}
