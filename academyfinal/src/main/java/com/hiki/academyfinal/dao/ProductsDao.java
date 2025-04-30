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

    public void insert(ProductsDto dto) { // ✅ void로 변경
        sqlSession.insert("products.add", dto); // ✅ insert만 수행
    }

    public int delete(Integer productNo) {
        return sqlSession.delete("products.delete", productNo);
    }

    public Integer update(ProductsDto dto) {
        return sqlSession.update("products.update", dto);
    }

    public int sequence() {
        return sqlSession.selectOne("products.sequence");
    }
 // 향 계열별 상품 조회 추가
    public List<ProductsDto> selectByAccord(int accordNo) {
        return sqlSession.selectList("products.selectByAccord", accordNo);
    }
    
    
}
