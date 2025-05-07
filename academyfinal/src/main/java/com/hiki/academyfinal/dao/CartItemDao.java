package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
	@Autowired
	private SqlSession sqlSession;
	
	
	
	public void deleteByProductNo(int productNo) {
		sqlSession.delete("cart_items.deleteByProductNo",productNo);
	}
}
