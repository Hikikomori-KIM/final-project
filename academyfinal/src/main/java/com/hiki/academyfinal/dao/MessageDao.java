package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.websocket.MessageDto;
import com.hiki.academyfinal.dto.websocket.MessageViewDto;

@Repository
public class MessageDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public MessageDto add(MessageDto messageDto) {
		long messageNo = sqlSession.selectOne("message.sequence");
		messageDto.setMessageNo(messageNo);
		sqlSession.insert("message.add", messageDto);
		return messageDto;
	}
	
	public List<MessageViewDto> selectListForAdmin(String usersType) {
 		return sqlSession.selectList("message.listForAdmin", usersType);
 	}
	
	public List<MessageViewDto> selectListForUsers(String usersType) {
		return sqlSession.selectList("message.listForUsers", usersType);
	}
}
