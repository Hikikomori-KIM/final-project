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
		long messageNo = sqlSession.selectOne("messageList.sequence");
		messageDto.setMessageNo(messageNo);
		sqlSession.insert("messageList.add", messageDto);
		return messageDto;
	} 
	
	public List<MessageViewDto> listByRoom(Long roomNo, Long messageNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("roomNo", roomNo);
		params.put("messageNo", messageNo);
		return sqlSession.selectList("messageList.listByRoom", params);
	}

	public List<MessageViewDto> selectListByPaging(String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		return sqlSession.selectList("messageList.listByPaging", params);
	}
	
	public List<MessageViewDto> selectListByPaging(String usersId, Long roomNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		params.put("roomNo", roomNo);
		return sqlSession.selectList("messageList.listByPaging", params);
	}
	
	public int cntByPaging(String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		return sqlSession.selectOne("messageList.cntByPaging", params);
	}
	
	public int cntByPaging(String usersId, Long roomNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("usersId", usersId);
		params.put("roomNo", roomNo);
		return sqlSession.selectOne("messageList.cntByPaging", params);
	}
	
	// message 테이블에 있는 RoomNo가 같은 메시지만 불러오도록 처리
	public boolean checkMessageAccess(Long messageNo, Long roomNo, String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("messageNo", messageNo);
		params.put("roomNo", roomNo);
		params.put("usersId", usersId);

		long count = sqlSession.selectOne("messageList.check", params);
		return count > 0;
	}

}