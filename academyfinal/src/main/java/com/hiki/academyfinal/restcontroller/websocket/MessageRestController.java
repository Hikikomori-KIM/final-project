package com.hiki.academyfinal.restcontroller.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.configuration.NaverLoginProperties;
import com.hiki.academyfinal.dao.websocket.MessageDao;
import com.hiki.academyfinal.dto.websocket.MessageViewDto;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.util.MessageConverter;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.websocket.MessageListVO;
import com.hiki.academyfinal.vo.websocket.MessageVO;

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
	
	@GetMapping("/room/{roomNo}")
	public MessageListVO listByRoomNo(
	        @RequestHeader(value = "Authorization", required = false) String bearerToken,
	        @PathVariable long roomNo) {

	    ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
	    List<MessageViewDto> list = messageDao.selectListByPaging(claimVO.getUsersId(), roomNo);
	    List<MessageVO> convertList = messageConverter.convertMessageFormat(list, claimVO.getUsersId());
	    return MessageListVO.builder()
	            .last(true) // 페이징 필요하면 false
	            .list(convertList)
	            .build();
	}
}
