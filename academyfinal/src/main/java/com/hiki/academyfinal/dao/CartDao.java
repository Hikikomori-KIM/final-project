package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CartDto;

@Repository
public class CartDao {
	@Autowired
	private SqlSession sqlSession;
	
	//본인 카트리스트
	public List<CartDto> SelectList(String usersId) {
		return sqlSession.selectList("cart.list", usersId);
	}
	
	//본인 카트1개조회(디테일)
	public CartDto selectOne(String usersId, Long cartNo) {
		Map<String,Object>result = new HashMap<>();
		result.put("usersId", usersId);
		result.put("cartNo", cartNo);
		return sqlSession.selectOne("cart.selectOne", result);
	}	
	//오버로딩
	public CartDto selectOne(CartDto cartDto) {
		return sqlSession.selectOne("cart.selectOne", cartDto);
	}
	
	//insert전에 중복조회 //필여하면오버로딩
	public boolean duplication(String usersId, Long cartItemNo) {
		Map<String,Object> result = new HashMap<>();
		result.put("usersId",usersId);
		result.put("cartItemNo", cartItemNo);
		return sqlSession.selectOne("cart.selectProduct",result);
	}
	
	//중복이라면 qty만바꿔주기
	public void updateQty(String usersId, int cartQty, Long cartNo) {
		Map<String,Object> result = new HashMap<>();
		result.put("usersId", usersId);
		result.put("cartQty",cartQty);
		result.put("cartNo", cartNo);
		sqlSession.update("cart.qtyUp",result);
	}
	//오버로딩 
	public void updateQty(CartDto cartDto) {
		sqlSession.update("cart.qtyUp",cartDto);
	}
	
	//기본인서트 //반환형바꿔도됌
	public void insert(CartDto cartDto) {
		Long sequence = sqlSession.selectOne("cart.sequence");
		cartDto.setCartNo(sequence);
		sqlSession.insert("cart.createCart",cartDto);
	}
	
	//단일삭제
	public boolean deleteOne(String usersId, Long cartNo) {
		return sqlSession.delete("cart.delete",cartNo) >0;
	}
	
	//여러개삭제 쿼리공부중
//	public boolean deleteMultiple(String usersId, List<Long> cartNoList) {
//	    Map<String, Object> params = new HashMap<>();
//	    params.put("cartNoList", cartNoList);
//	    return sqlSession.delete("cart.deleteList", params) > 0;
//	}
}
