package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ProductDetailViewDto;
import com.hiki.academyfinal.dto.ScentRecommendationDto;
import com.hiki.academyfinal.vo.MachingListVO;

@Repository
public class ScentRecommendationDao {

	@Autowired
	private SqlSession sqlSession;

	public List<MachingListVO> list(){
		return sqlSession.selectList("scentRecommendation.list");
	}
	//아이템만수정
	public boolean changeProduct(long productNo, int scentRecommendationNo) {
		Map<String,Object> result = new HashMap<>();
		result.put("productNo", productNo);
		result.put("scentRecommendationNo", scentRecommendationNo);
		return sqlSession.update("scentRecommendation.changeProduct", result)>0;
	}
	//코멘트만수정 ㅋㅋ
	public boolean changeComment(int scentRecommendationNo,String scentRecommendationComment) {
		Map<String,Object> result = new HashMap<>();
		result.put("scentRecommendationNo", scentRecommendationNo);
		result.put("scentRecommendationComment", scentRecommendationComment);
		return sqlSession.update("scentRecommendation.changeComment", result)>0;
	}
	
	public ProductDetailViewDto product(String topScoreType) {
		 int no = sqlSession.selectOne("scentRecommendation.findProduct",topScoreType);
		return  sqlSession.selectOne("scentRecommendation.product",no);
	}
	
	public ScentRecommendationDto dto(String topScoreType) {
		return sqlSession.selectOne("scentRecommendation.finalData",topScoreType);
	}
}
