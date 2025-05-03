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
import com.hiki.academyfinal.dto.websocket.MessageDto;
import com.hiki.academyfinal.dto.websocket.RoomDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/room")
public class RoomRestController {
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/") // 방 생성
	public void create(@RequestBody RoomDto roomDto,
			@RequestHeader("Authorization") String bearerToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		String usersId = claimVO.getUsersId();
		
		roomDto.setRoomOwner(usersId); // 소유자 설정 (roomOwner = 문의고객 ID)
		roomDto.setRoomTitle("[문의·상담] " + usersId);
		
		roomDao.insert(roomDto);
	}
	
	@GetMapping("/") // 방 리스트업
	public List<RoomDto> list() {
	    return roomDao.selectList();
	}

	@GetMapping("/{roomNo}")
	public RoomDto find(@PathVariable long roomNo) {
		RoomDto roomDto = roomDao.selectOne(roomNo);
		if(roomDto == null) throw new TargetNotFoundException("방번호 오류");
		return roomDto;
	}
	
	@DeleteMapping("/leave/{roomNo}")
	public void leave(@PathVariable long roomNo,
			@RequestHeader("Authorization") String bearerToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		
	    System.out.println("usersId: " + claimVO.getUsersId()); // usersId 확인
	    System.out.println("usersType: " + claimVO.getUsersType()); // usersType 확인
		
		RoomDto roomDto = roomDao.selectOne(roomNo);
		if(roomDto == null) throw new TargetNotFoundException("방번호 오류");
		if (!(roomDto.getRoomOwner().equals(claimVO.getUsersId()) || claimVO.getUsersType().equals("관리자")))
		    throw new TargetNotFoundException("삭제 권한 없음 (해당 고객 혹은 관리자 아님)");
		roomDao.leaveRoom(roomNo, claimVO.getUsersId());
	}
	
	@PostMapping("/enter/{roomNo}")
	public void enter(@RequestHeader("Authorization") String bearerToken,
			@PathVariable long roomNo) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		boolean isEnter = roomDao.checkRoom(roomNo, claimVO.getUsersId());
		if(isEnter == false) {
			roomDao.enterRoom(roomNo, claimVO.getUsersId());
		}
	}
	
	@GetMapping("/check/{roomNo}")
	public boolean check(@PathVariable long roomNo,
	                     @RequestHeader("Authorization") String bearerToken) {
	    ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
	    RoomDto roomDto = roomDao.selectOne(roomNo);
	    return roomDto.getRoomOwner().equals(claimVO.getUsersId()) || claimVO.getUsersType().equals("관리자");
	    // return roomDao.checkRoom(roomNo, claimVO.getUsersId());
	    // 이전코드: roomOwner와 usersId만 비교하므로 usersType의 비교는 되고 있지 않음 (최종 수정 완)
	}
}
