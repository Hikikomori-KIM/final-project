package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ProductAccordDto;

@Repository
public class ProductAccordDao {

    @Autowired
    private SqlSession sqlSession;

    public void insert(ProductAccordDto dto) {
        sqlSession.insert("productAccord.insert", dto);
    }
    public void deleteByProductNo(long productNo) {
        sqlSession.delete("productAccord.deleteByProductNo", productNo);
    }
}
