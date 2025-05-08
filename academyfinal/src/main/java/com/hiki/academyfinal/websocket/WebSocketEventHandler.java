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

@Slf4j
@Service
public class WebSocketEventHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private MessageDao messageDao;

    // 세션 -> 방번호
    private Map<String, Long> roomSessions = Collections.synchronizedMap(new HashMap<>());

    // 세션 -> 사용자 ID
    private Map<String, String> roomUsers = Collections.synchronizedMap(new HashMap<>());

    @EventListener
    public void userSubscribe(SessionSubscribeEvent event) {
        log.debug("채널 구독됨");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination(); // 예: "/private/group/chat/62"
        log.debug("구독 채널 조회 = {}", destination);

        if (destination == null) return;
        if (!destination.startsWith("/private/group/chat/")) return;

        try {
            // 채널에서 roomNo 추출
            String roomNoStr = destination.substring(destination.lastIndexOf("/") + 1);
            long roomNo = Long.parseLong(roomNoStr);

            // 토큰 파싱
            String accessToken = accessor.getFirstNativeHeader("accessToken");
            log.debug("accessToken = {}", accessToken);

            ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
            String usersId = claimVO.getUsersId();

            // ENTER 메시지 전송
            messagingTemplate.convertAndSend(
                "/private/group/chat/" + roomNo,
                ActionVO.builder()
                    .type("ENTER")
                    .usersId(usersId)
                    .build()
            );

            // 세션 상태 저장
            roomSessions.put(accessor.getSessionId(), roomNo);
            roomUsers.put(accessor.getSessionId(), usersId);

        } catch (Exception e) {
            log.error("userSubscribe 처리 중 예외 발생", e);
        }
    }

    @EventListener
    public void userLeave(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        Long roomNo = roomSessions.remove(sessionId);
        String usersId = roomUsers.remove(sessionId);

        if (roomNo == null || usersId == null) return;

        // LEAVE 메시지 전송
        messagingTemplate.convertAndSend(
            "/private/group/chat/" + roomNo,
            ActionVO.builder()
                .type("LEAVE")
                .usersId(usersId)
                .build()
        );
    }
}
