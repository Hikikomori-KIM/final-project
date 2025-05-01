package com.hiki.academyfinal.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/chat");
		// /chat 을 포함한 어떤 채널도 사용 가능
		registry.setApplicationDestinationPrefixes("/app");
		// /app 으로 시작하는 채널로 메시지를 보내면 수신하겠음을 의미
	}
	 @Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws") // 웹소켓 연결 생성 주소
			.setAllowedOriginPatterns("*") // 허용가능한 클라이언트 주소 패턴
			.withSockJS();
	}
}
