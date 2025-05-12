package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ReviewDto;

@Repository
public class ReviewDao {

    @Autowired
    private SqlSession sqlSession;

    // 리뷰 등록
    public void insertReview(ReviewDto dto) {
        long reviewNo = sqlSession.selectOne("review.sequence");
        dto.setReviewNo(reviewNo);
        sqlSession.insert("review.insertReview", dto);
    }

    // 리뷰 이미지 등록
    public void insertReviewImage(long reviewNo, long attachmentNo) {
        var param = new java.util.HashMap<String, Object>();
        param.put("reviewNo", reviewNo);
        param.put("attachmentNo", attachmentNo);
        sqlSession.insert("review.insertReviewImage", param);
    }

    // 특정 상품의 리뷰 목록
    public List<ReviewDto> selectReviewList(long productNo) {
        return sqlSession.selectList("review.selectReviewList", productNo);
    }

    public List<Long> selectAttachmentNosByReviewNo(long reviewNo) {
        return sqlSession.selectList("review.selectAttachmentNosByReviewNo", reviewNo);
    }

    public void deleteReviewImagesByReviewNo(long reviewNo) {
        sqlSession.delete("review.deleteReviewImagesByReviewNo", reviewNo);
    }

    public void deleteReviewByReviewNo(long reviewNo) {
        sqlSession.delete("review.deleteReviewByReviewNo", reviewNo);
    }
    public boolean adminDeleteReview(long reviewNo) {
        return sqlSession.delete("review.adminDeleteReview", reviewNo) > 0;
    }



}
