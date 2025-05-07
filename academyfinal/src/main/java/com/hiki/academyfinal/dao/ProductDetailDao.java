package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.VolumeDto;
import com.hiki.academyfinal.vo.ProductDetailVO;

@Repository
public class ProductDetailDao {

    @Autowired
    private SqlSession sqlSession;

    // ✅ 단일 상세 정보 조회 (ProductDetailVO)
    public List<ProductDetailVO> selectDetail(int productNo) {
        return sqlSession.selectList("productDetail.selectDetail", productNo);
    }

    // ✅ 해당 상품의 용량 목록 조회 (VolumeDto List)
    public List<VolumeDto> selectVolumes(int productNo) {
        return sqlSession.selectList("volume.selectVolumes", productNo);
    }
 // 향 계열 이름 목록 조회
    public List<String> selectAccords(int productNo) {
        return sqlSession.selectList("productDetail.selectAccords", productNo);
    }
    //삭제
    public void deleteByProductNo(int productNo) {
    	sqlSession.delete("perfumeDetail.deleteByProductNo",productNo);
    }
    
}
