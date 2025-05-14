package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.vo.MachingPageVO;
import com.hiki.academyfinal.vo.MachingResponseVO;
import com.hiki.academyfinal.vo.ProductListVO;
import com.hiki.academyfinal.vo.ProductSalesVO;

@Repository
public class ProductsDao {
    @Autowired
    private SqlSession sqlSession;

 // ✅ VO 기반으로 경량 리스트 조회 (null-safe 방식으로 변경)
    public List<ProductListVO> selectListVO(Integer accordNo, Integer categoryNo) {
        Map<String, Object> param = new java.util.HashMap<>();
        if (accordNo != null) param.put("accordNo", accordNo);
        if (categoryNo != null) param.put("categoryNo", categoryNo);
        return sqlSession.selectList("products.listVO", param);
    }
    
    public ProductsDto selectOne(long productNo) {
        return sqlSession.selectOne("products.find", productNo);
    }
    
    public ProductsDto selectOne(ProductsDto productsDto) {
    	return sqlSession.selectOne("products.find", productsDto);
    }

    public void insert(ProductsDto dto) {
        sqlSession.insert("products.add", dto);
    }

    public int delete(long productNo) {
        return sqlSession.delete("products.delete", productNo);
    }

    public Integer update(ProductsDto dto) {
        return sqlSession.update("products.update", dto);
    }

    public int sequence() {
        return sqlSession.selectOne("products.sequence");
    }

    // ✅ HTML 저장 (쿼리 ID 수정)
    public void updateProductHtml(long productNo, String html) {
        sqlSession.update("products.updateProductHtml", Map.of(
            "productNo", productNo,
            "html", html
        ));
    }

    public String findProductHtml(long productNo) {
        return sqlSession.selectOne("products.findProductHtml", productNo);
    }
    
 // 🔥 카테고리별 상품 목록 조회
    public List<ProductListVO> selectByCategory(int categoryNo) {
        return sqlSession.selectList("products.listVO", Map.of("categoryNo", categoryNo));
    }
    // 베스트 상품 목록 조회
	public List<ProductSalesVO> selectBestProducts(){
		return sqlSession.selectList("products.selectBestProducts");
	}
	public void updateMdPick(long productNo, String mdPick) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("productNo", productNo);
	    param.put("mdPick", mdPick);
	    sqlSession.update("products.updateMdPick", param);
	}

	
	
//	송시우가필요한데이터 검색&페이징
	public List<MachingResponseVO> productList(MachingPageVO vo){
		List<MachingResponseVO> list= sqlSession.selectList("products.listTopNQuery",vo);
//		System.out.println("vo"+list);
		return list;
	}
	//카운트
	public int count() {
		int count = sqlSession.selectOne("products.count");
		return count;
	}

	//최신상품 페이지
	public List<ProductListVO> selectNewProducts(){
		return sqlSession.selectList("products.getNewProducts");
	}
	//할인상품 페이지
	public List<ProductListVO> selectSpecialPriceProducts(){
		return sqlSession.selectList("products.getSpecialPriceProducts");
	}


}
