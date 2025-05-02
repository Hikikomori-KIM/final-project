package com.hiki.academyfinal.dao.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.configuration.EmailConfiguration;
import com.hiki.academyfinal.dto.websocket.RoomDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RoomDao {
	@Autowired
	private SqlSession sqlSession;

	
	public RoomDto insert(RoomDto roomDto) {
		long roomNo = sqlSession.selectOne("room.sequence");
		roomDto.setRoomNo(roomNo);
		sqlSession.insert("room.create", roomDto);
		return roomDto;
	} // 방 생성 (고객이 생성 가능))
	
	public List<RoomDto> selectList() {
		return sqlSession.selectList("room.list");
	} // 방 리스트 (관리자 창에서 list-up)
	
	public RoomDto selectOne(long roomNo) {
		return sqlSession.selectOne("room.find", roomNo);
	}	
	
	public void enterRoom(long roomNo, String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("roomNo", roomNo);
		params.put("usersId", usersId);
		sqlSession.insert("room.enter", params);
	}

	public boolean checkRoom(long roomNo, String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("roomNo", roomNo);
		params.put("usersId", usersId);
		long count = sqlSession.selectOne("room.check", params);
		return count > 0;
	}
	 
	public boolean leaveRoom(long roomNo, String usersId) {
		Map<String, Object> params = new HashMap<>();
		params.put("roomNo", roomNo);
		params.put("usersId", usersId);
		return sqlSession.delete("room.leave", params) > 0;
	}
}
