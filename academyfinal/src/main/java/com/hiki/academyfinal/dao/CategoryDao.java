package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.CategoryDto;

@Repository
public class CategoryDao {
	@Autowired
	SqlSession sqlSession;
	
	public CategoryDto insert(CategoryDto categoriesDto) {
		int categoryNo =sqlSession.selectOne("category.sequence");
		categoriesDto.setCategoryNo(categoryNo);
		sqlSession.insert("category.add",categoriesDto);
		return categoriesDto;
	}
	public List<CategoryDto> selectList(){
		return sqlSession.selectList("category.selectList");
	}
	
//	public CategoryDto update(int categoryNo) {
//		return 
//	}
}
