package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CategoryDto;

@Repository
public class CategoryDao {

    @Autowired
    private SqlSession sqlSession;

    // 🔹 시퀀스 미리 조회 (insert 시 사용)
    public int sequence() {
        return sqlSession.selectOne("category.sequence");
    }

    // 🔹 카테고리 등록
    public CategoryDto insert(CategoryDto dto) {
        int categoryNo = sequence();
        dto.setCategoryNo(categoryNo);
        sqlSession.insert("category.add", dto);
        return dto;
    }

    // 🔹 카테고리 전체 조회
    public List<CategoryDto> selectList() {
        return sqlSession.selectList("category.selectList");
    }

    // 🔹 카테고리 수정
    public void update(CategoryDto dto) {
        sqlSession.update("category.update", dto);
    }

    // 🔹 카테고리 삭제
    public void delete(int categoryNo) {
        sqlSession.delete("category.delete", categoryNo);
    }
}
