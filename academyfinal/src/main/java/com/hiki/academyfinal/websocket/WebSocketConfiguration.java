package com.hiki.academyfinal.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	// 웹소켓 기본 설정 파일
	// : publish를 통해서 특정 채널에 메시지 전달 (구독이랑 다름)
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/private");
		// 구독가능한 채널의 접두사: /private을 포함한 그 뒤의 어떤 채널도 사용가능
		registry.setApplicationDestinationPrefixes("/app");
		// 사용자가 메시지를 보낼 수 있는 채널 : /app으로 시작하는 채널로 메시지를 보내면 수신함
	}
	 @Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws") // 웹소켓 연결 생성 주소 (접속 주소)
			.setAllowedOriginPatterns("*") // 허용가능한 클라이언트 주소 패턴 (= CORS)
			.withSockJS(); // ws를 http처럼 쓸 수 있도록 함 
	}
}
