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
}
