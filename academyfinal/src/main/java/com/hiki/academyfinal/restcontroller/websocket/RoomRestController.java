package com.hiki.academyfinal.restcontroller.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.websocket.RoomDao;
import com.hiki.academyfinal.dto.websocket.RoomDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;

@CrossOrigin
@RestController
@RequestMapping("/api/room")
public class RoomRestController {
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/")
	public void create(@RequestBody RoomDto roomDto,
			@RequestHeader("Authorization")String bearerToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		roomDto.setRoomOwner(claimVO.getUsersId()); // 소유자 설정
		roomDao.insert(roomDto);
	}
	
	@GetMapping("/")
	public List<RoomDto> list() {
		return roomDao.selectList();
	}
	
	@GetMapping("/{roomNo}")
	public RoomDto find(@PathVariable long roomNo) {
		RoomDto roomDto = roomDao.selectOne(roomNo);
		if(roomDto == null) throw new TargetNotFoundException("방 번호가 존재하지 않습니다");
		return roomDto;
	}
	
	@DeleteMapping("/{roomNo}")
	public void delete(@PathVariable long roomNo,
			@RequestHeader("Authorization")String bearerToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		RoomDto roomDto = roomDao.selectOne(roomNo);
		if(roomDto == null) throw new TargetNotFoundException("방 번호가 존재하지 않습니다");
		if(roomDto.getRoomOwner().equals(claimVO.getUsersId()) == false)
			throw new TargetNotFoundException("소유자 불일치");
		roomDao.delete(roomNo);
	}
}
