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
	
	//시퀀스 조회
	public long sequence() {
		return sqlSession.selectOne("accord.sequence");
	}
	
	public List<AccordDto> selectList(){
		return sqlSession.selectList("accord.findAll");
	}

    public Integer findAccordNoByName(String accordName) {
        return sqlSession.selectOne("accord.findNoByName", accordName);
    }
    //어코드 수정
    public void update(AccordDto accordDto) {
    	sqlSession.update("accord.update",accordDto);
    }
    //어코드 삭제
    public void delete(long accordNo) {
    	sqlSession.delete("accord.delete",accordNo);
    }
    //어코드 추가
    public AccordDto insert(AccordDto accordDto) {
    	long accordNo = sequence();
    	accordDto.setAccordNo(accordNo);
    	sqlSession.insert("accord.add",accordDto);
    	return accordDto;
    }
}
