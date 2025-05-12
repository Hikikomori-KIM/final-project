package com.hiki.academyfinal.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductImgDao {
	@Autowired
	public SqlSession sqlSession;
	
	public void insert(long productNo,long attachmentNo,String imageType) {
		Map<String,Object> param = Map.of(
				"productNo",productNo,
				"attachmentNo",attachmentNo,
				"imageType",imageType
		);
		sqlSession.insert("productImg.add",param);
	}
	
	public Integer findAttachmentNoByProductNo(long productNo) {
	    return sqlSession.selectOne("productImg.findAttachmentNoByProductNo", productNo);
	}
	
	public void deleteByProductNo(long productNo) {
		sqlSession.delete("productImg.deleteByProductNo",productNo);
	}
}
