package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CartDto;
import com.hiki.academyfinal.vo.CartViewVO;
import com.hiki.academyfinal.vo.PageVO;

@Repository
public class CartDao {
	@Autowired
	private SqlSession sqlSession;
	
	//본인 카트리스트
	public List<CartViewVO> SelectList(String usersId, PageVO pageVO) {
		Map<String,Object> result = new HashMap<>();
		result.put("usersId", usersId);
		result.put("start", pageVO.getStart());
		result.put("end", pageVO.getEnd());
		  System.out.println("usersId: " + usersId); 
		   System.out.println("PageVO: " + pageVO);  
		return sqlSession.selectList("cart.list", result);
	}
	
	//본인 카트1개조회(디테일)
	public CartViewVO selectOne(String usersId, Long cartNo) {
		Map<String,Object>result = new HashMap<>();
		result.put("usersId", usersId);
		result.put("cartNo", cartNo);
		return sqlSession.selectOne("cart.selectOne", result);
	}	
	//오버로딩
	public CartViewVO selectOne(CartDto cartDto) {
		return sqlSession.selectOne("cart.selectOne", cartDto);
	}
	
	//insert전에 중복조회 후 qty변경 or create
	public void duplicationAndCreate(List<CartDto> cartList) {
		for(CartDto cartDto : cartList) {
		Map<String,Object> result = new HashMap<>();
		result.put("usersId",cartDto.getUsersId());
		result.put("cartItemNo", cartDto.getCartItemNo());
		result.put("volumeNo", cartDto.getVolumeNo());
		CartDto findCart =  sqlSession.selectOne("cart.selectProduct",result);
//		System.out.println("cartDto:"+cartDto);
//		System.out.println("findCart:"+findCart);
		if(findCart != null) {
			int qty = cartDto.getCartQty() + findCart.getCartQty();
			findCart.setCartQty(qty);
			updateQty(findCart);
		}
		else {
//			System.out.println(cartDto);
			insert(cartDto);
		}
		}
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
	//상품이 삭제될때 장바구니 속 상품까지 삭제하는 메서드
	public void deleteByProductNo(long productNo) {
	    sqlSession.delete("cart.deleteByProductNo", productNo);
	}
	// ✅ volumeNo로 장바구니 항목 삭제
	public void deleteByVolumeNo(long volumeNo) {
	    sqlSession.delete("cart.deleteByVolumeNo", volumeNo);
	}
	//여러개삭제 쿼리공부중
//	public boolean deleteMultiple(String usersId, List<Long> cartNoList) {
//	    Map<String, Object> params = new HashMap<>();
//	    params.put("cartNoList", cartNoList);
//	    return sqlSession.delete("cart.deleteList", params) > 0;
//	}
}
