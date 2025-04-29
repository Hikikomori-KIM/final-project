package com.hiki.academyfinal.dao.websocket;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.configuration.EmailConfiguration;
import com.hiki.academyfinal.dto.websocket.RoomDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RoomDao {

    private final EmailConfiguration emailConfiguration;
	@Autowired
	private SqlSession sqlSession;

    RoomDao(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }
	
	public RoomDto insert(RoomDto roomDto) {
		int roomNo = sqlSession.selectOne("room.sequence");
		roomDto.setRoomNo(roomNo);
		sqlSession.insert("room.create", roomDto);
		return roomDto;
	}
	
	public List<RoomDto> selectList() {
		return sqlSession.selectList("room.list");
	}
	
	public RoomDto selectOne(long roomNo) {
		return sqlSession.selectOne("room.find", roomNo);
	}
	
	public boolean delete(long roomNo) {
		return sqlSession.delete("room.delete", roomNo) > 0;
	}
}
