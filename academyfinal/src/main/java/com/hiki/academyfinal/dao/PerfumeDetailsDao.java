package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.PerfumeDetailsDto;

@Repository
public class PerfumeDetailsDao {
	@Autowired
	SqlSession sqlSession;
	
	//시퀀스 발급
	public int sequence() {
		return sqlSession.selectOne("perfumeDetails.sequence");
	}
	
	//향수 정보 등록
	public void insert(PerfumeDetailsDto perfumeDetailsDto) {
		sqlSession.insert("perfumeDetails.add",perfumeDetailsDto);
	}
	
	public void deleteByProductNo(int productNo) {
		sqlSession.delete("perfumeDetails.deleteByProductNo",productNo);
	}
	
	
	
}
