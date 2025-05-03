package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ProductDetailViewDto;

@Repository
public class ProductDetailViewDao {

	@Autowired
	private SqlSession sqlSession;
	
	public ProductDetailViewDto productInfo(long productNo) {
		return sqlSession.selectOne("productDetailView.selectOne", productNo);
	}
}
