package com.hiki.academyfinal.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.websocket.ChatResponseVO;
import com.hiki.academyfinal.vo.websocket.ChatVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UsersDao usersDao;
	
	@MessageMapping("/group/chat/{roomNo}")
	public void chat(@DestinationVariable long roomNo,
			Message<ChatVO> message) {
		// 헤더 분석
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		if(accessToken == null || accessToken.startsWith("Bearer ") == false) return;
		
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		UsersDto usersDto = usersDao.selectOne(claimVO.getUsersId());
		if(usersDto == null) return;
		
		ChatVO vo = message.getPayload();
		
		ChatResponseVO response = ChatResponseVO.builder()
					.type("CHAT")
					.usersId(usersDto.getUsersId())
					.usersName(usersDto.getUsersName())
					.usersType(usersDto.getUsersType())
					.content(vo.getContent())
					.time(LocalDateTime.now())
				.build();
		
		// 최종 전송
		messagingTemplate.convertAndSend("/private/group/chat/" + roomNo, response);
	}
}
