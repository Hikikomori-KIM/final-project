package com.hiki.academyfinal.vo.websocket;

import lombok.Data;

@Data
public class ChatVO {
	private String content;
	private Long roomNo;
	// 사용자가 채팅을 보낼 때, 어떤 채팅방인지 알아야 하므로 필요
}
