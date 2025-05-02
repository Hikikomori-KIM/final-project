package com.hiki.academyfinal.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dao.websocket.MessageDao;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.websocket.ActionVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j @Service
public class WebSocketEventHandler {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate; // 전송 도구
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UsersDao usersDao;
	@Autowired
	private MessageDao messageDao;
	
	// 세션이 어느 방에 속해있는지 알기 위한 저장소
	private Map<String, Long> roomSessions = Collections.synchronizedMap(new HashMap<>());
	// 해당 세션의 사용자 ID를 저장
	private Map<String, String> roomUsers = Collections.synchronizedMap(new HashMap<>());
	
	@EventListener
	public void userSubscribe(SessionSubscribeEvent event) {
		log.debug("채널 구독됨");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		log.debug("구독 채널 조회 = {}", accessor.getDestination());
		if(accessor.getDestination() == null) return;
		if(accessor.getDestination().startsWith("/private/group/chat/")) {
			// roomNo 추출
			int position = "/private/group/chat".length();
			String roomStr = accessor.getDestination().substring(position);
			long roomNo = Long.parseLong(roomStr);
			// access Token 추출
			String accessToken = accessor.getFirstNativeHeader("accessToken");
			log.debug("accessToken = {}", accessToken);
			ClaimVO claimVO = tokenService.parseBearerToken(accessToken);

			messagingTemplate.convertAndSend(
					"/private/group/chat/"+roomNo,
					ActionVO.builder()
						.usersId(claimVO.getUsersId())
				.build());
			
			messagingTemplate.convertAndSend(
					"/private/group/chat/"+roomNo,
					ActionVO.builder()
						.usersId(claimVO.getUsersId())
				.build()
			);
			// 세션 번호, 방 번호 기억
			roomSessions.put(accessor.getSessionId(), roomNo);
			roomUsers.put(accessor.getSessionId(), claimVO.getUsersId());
		}
	}
	
	@EventListener
	public void userLeave(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId(); // 웹소켓 세션 ID
		Long roomNo = roomSessions.remove(sessionId);
		String usersId = roomUsers.remove(sessionId);
		if(roomNo == null) return;
		if(usersId == null) return;
		
		messagingTemplate.convertAndSend(
			"/private/group/chat/"+roomNo, 
			ActionVO.builder()
				.usersId(usersId)
			.build()
		);
	}
}
