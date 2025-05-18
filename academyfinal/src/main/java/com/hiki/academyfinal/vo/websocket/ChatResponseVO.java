package com.hiki.academyfinal.vo.websocket;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChatResponseVO implements MessageVO {
	private Long messageNo;
	private String usersId;
	private String usersName;
	private String usersType;
	private String content;
	private String type;
	private LocalDateTime time;
	
	private Long roomNo;
	// 클라이언트에 메시지를 보낼 때 어떤 방의 메시지인지 알려줘야 함
}
