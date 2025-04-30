package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.AccordDto;

@Repository
public class AccordDao {
	@Autowired
	private SqlSession sqlSession;
	
	public List<AccordDto> selectList(){
		return sqlSession.selectList("accord.findAll");
	}

    public Integer findAccordNoByName(String accordName) {
        return sqlSession.selectOne("accord.findNoByName", accordName);
    }
    
}
