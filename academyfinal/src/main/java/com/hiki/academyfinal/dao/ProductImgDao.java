package com.hiki.academyfinal.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductImgDao {
	@Autowired
	public SqlSession sqlSession;
	
	public void insert(int productNo,int attachmentNo) {
		Map<String,Object> param = Map.of(
				"productNo",productNo,
				"attachmentNo",attachmentNo
		);
		sqlSession.insert("productImg.add",param);
	}
}
