package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewsDao {
	@Autowired
	private SqlSession sqlSession;
	
	
	public void deleteByProductNo(long productNo) {
		sqlSession.delete("reviews.deleteByProductNo",productNo);
	}
	
	
}
