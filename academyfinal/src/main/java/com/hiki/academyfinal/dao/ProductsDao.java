package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ProductsDto;

@Repository
public class ProductsDao {
    @Autowired
    private SqlSession sqlSession;

    public List<ProductsDto> selectList() {
        return sqlSession.selectList("products.list");
    }

    public ProductsDto selectOne(int productNo) {
        return sqlSession.selectOne("products.find", productNo);
    }

    public ProductsDto insert(ProductsDto dto) {
        Integer sequence = sqlSession.selectOne("products.sequence");
        dto.setProductNo(sequence);
        sqlSession.insert("products.add", dto);
        return sqlSession.selectOne("products.find", sequence);
    }

    public int delete(Integer productNo) {
        return sqlSession.delete("products.delete", productNo);
    }

    public Integer update(ProductsDto dto) {
        return sqlSession.update("products.update", dto);
    }
    public int sequence() {
    	return  sqlSession.selectOne("products.sequence");
    }
}
