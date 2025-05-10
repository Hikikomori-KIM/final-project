package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.ScentListViewDto;

@Repository
public class ScentListViewDao {

	@Autowired
	private SqlSession sqlSession;
	
	public List<ScentListViewDto> scentList(){
		return sqlSession.selectList("scentListView.list");
	}
}
