package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.NoticeDto;

@Repository
public class NoticeDao {
	@Autowired
	private SqlSession sqlSession;
	
	public List<NoticeDto> selectList() {
		return sqlSession.selectList("notice.list");
	}
	
	public NoticeDto selectOne(long noticeNo) {
		return sqlSession.selectOne("notice.detail", noticeNo);
	}
	
	public NoticeDto selectOne(NoticeDto noticeDto) {
		return sqlSession.selectOne("notice.detail", noticeDto);
	}
	
}
