package com.hiki.academyfinal.dao.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public List<MessageViewDto> selectList(String usersId) {
		return sqlSession.selectList("message.list", usersId);
	}
	public List<MessageViewDto> selectListByPaging(String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		return sqlSession.selectList("message.listByPaging", params);
	}
	public int cntByPaging(String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		return sqlSession.selectOne("message.cntByPaging", params);
	}
	public int cntByPaging(String usersId, long messageNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		params.put("messageNo", messageNo);
		return sqlSession.selectOne("message.cntByPaging", params);
	}
}