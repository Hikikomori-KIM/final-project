package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CartDto;

@Repository
public class CartDao {
	@Autowired
	private SqlSession sqlSession;
	
	public List<CartDto> SelectList() {
		return sqlSession.selectList("cart.list");
	}
	
	public CartDto selectOne(long cartItemsNo) {
		return sqlSession.selectOne("cart.find", cartItemsNo);
	}	
	
	public CartDto selectOne(CartDto cartDto) {
		return sqlSession.selectOne("cart.find", cartDto);
	}
}
