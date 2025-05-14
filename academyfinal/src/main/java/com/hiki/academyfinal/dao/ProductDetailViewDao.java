package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ProductDetailViewDto;
import com.hiki.academyfinal.vo.MachingPageVO;
import com.hiki.academyfinal.vo.MachingResponseVO;

@Repository
public class ProductDetailViewDao {

	@Autowired
	private SqlSession sqlSession;
	
	public ProductDetailViewDto productInfo(long productNo) {
		return sqlSession.selectOne("productDetailView.selectOne", productNo);
	}
	//단일리스트
	public List<ProductDetailViewDto> productList2(){
		return sqlSession.selectList("productDetailView.selectList");
	}
	
	//검색 && 페이징 추가
	public List<MachingResponseVO> productList(MachingPageVO vo){
		List<MachingResponseVO> list= sqlSession.selectList("productDetailView.listTopNQuery",vo);
//		System.out.println("vo"+list);
		return list;
	}
	
	public int count() {
		int count = sqlSession.selectOne("productDetailView.count");
		return count;
	}
	
	
}
