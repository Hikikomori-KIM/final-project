package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.vo.AdminPageVO;
import com.hiki.academyfinal.vo.ManagementResponseVO;

@Repository
public class ManagementDao {

	@Autowired
	private SqlSession sqlSession;
	// adminPageVO = extends pageVO / adminType필요해서 
	public List<ManagementResponseVO> usersList(AdminPageVO adminPageVO){
		return sqlSession.selectList("users.usersList",adminPageVO);
	}
	
	public int count() {
		return sqlSession.selectOne("users.count");
	}
}
