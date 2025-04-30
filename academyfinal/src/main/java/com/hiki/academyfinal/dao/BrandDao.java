package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.BrandDto;

@Repository
public class BrandDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<BrandDto> selectList(){
		return sqlSession.selectList("brands.findAll");
	}
}
