package com.hiki.academyfinal.restcontroller.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hiki.academyfinal.dao.websocket.MessageDao;
import com.hiki.academyfinal.dao.websocket.RoomDao;
import com.hiki.academyfinal.dto.websocket.MessageDto;
import com.hiki.academyfinal.dto.websocket.MessageViewDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.util.MessageConverter;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.websocket.MessageListVO;
import com.hiki.academyfinal.vo.websocket.MessageVO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@CrossOrigin
@RestController
@RequestMapping("/api/message")
public class MessageRestController {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MessageConverter messageConverter;
	@Autowired
	private RoomDao roomDao;
	
	// 모든 메시지 목록
	@GetMapping("/")
	public MessageListVO list(@RequestHeader(value="Authorization", required=false) String bearerToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		List<MessageViewDto> list = messageDao.selectListByPaging(claimVO.getUsersId());
		List<MessageVO> convertList = messageConverter.convertMessageFormat(list, claimVO.getUsersId());
		int cnt = messageDao.cntByPaging(claimVO.getUsersId());
		boolean last = cnt==list.size();
		return MessageListVO.builder()
					.last(last).list(convertList)
				.build();
	}
	
	@PostMapping("/")
	public MessageDto addMessage(@RequestBody MessageDto messageDto,
			@RequestHeader(value="Authorization", required=false) String bearerToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		messageDto.setMessageSender(claimVO.getUsersId());
		if(messageDto.getRoomNo() == null)
			throw new TargetNotFoundException();
		MessageDto result = messageDao.add(messageDto);
		return result;
	}
	
	@GetMapping("/room")
	public MessageListVO listByRoomNo(@RequestHeader(value="Authorization", required=false) String bearerToken,
			@RequestParam long roomNo) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		System.out.println("Bearer Token: " + bearerToken);
		List<MessageViewDto> list = messageDao.selectListByPaging(claimVO.getUsersId(), roomNo);
		List<MessageVO> convertList = messageConverter.convertMessageFormat(list, claimVO.getUsersId());
		return MessageListVO.builder()
					.last(true) // 페이징이 필요하면 false
					.list(convertList)
				.build();
	}
	
	@GetMapping("/room/{roomNo}")
	public MessageListVO listByRoom(
	    @RequestHeader(value = "Authorization", required = false) String bearerToken,
	    @PathVariable long roomNo) {
		
	    ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
	    String usersId = claimVO.getUsersId();
	    
	    if(!"관리자".equals(claimVO.getUsersType()) ) { // 관리자인지 확인
	    	if(!roomDao.checkRoom(roomNo, usersId)) { // 해당 방의 오너인지 확인
	    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 방에 참여하실 수 없습니다");
	    	}	    	
	    }
	    // 참여자 메시지 호출
	    List<MessageViewDto> list = messageDao.selectListByPaging(claimVO.getUsersId(), roomNo);
	    List<MessageVO> convertList = messageConverter.convertMessageFormat(list, claimVO.getUsersId());
	    
	    return MessageListVO.builder()
	            .last(true)
	            .list(convertList)
	            .build();
	}
	
	@GetMapping("/{messageNo}")
	public MessageListVO listMore(
			@RequestHeader(value="Authorization", required=false) String bearerToken,
			@PathVariable long messageNo) {
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		List<MessageViewDto> list = messageDao.selectListByPaging(claimVO.getUsersId(), messageNo);
		List<MessageVO> convertList = messageConverter.convertMessageFormat(list, claimVO.getUsersId());
		int cnt = messageDao.cntByPaging(claimVO.getUsersId(), messageNo);
		boolean last = cnt==list.size();
		return MessageListVO.builder()
				.last(last).list(convertList)
				.build();
	}
	
}
