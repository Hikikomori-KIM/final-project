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
	
	public long sequence() {
		return sqlSession.selectOne("brands.sequence");
	}
	
	//리스트
	public List<BrandDto> selectList(){
		return sqlSession.selectList("brands.findAll");
	}
	//등록
	public BrandDto insert(BrandDto brandDto) {
		long brandNo = sequence();
		brandDto.setBrandNo(brandNo);
		sqlSession.insert("brands.add",brandDto);
		return brandDto;
	}
	//수정
	public void update(BrandDto brandDto) {
		sqlSession.update("brands.update",brandDto);
	}
	//삭제
	public void delete(int brandNo) {
		sqlSession.delete("brands.delete",brandNo);
	}
	
	
	
	
}
