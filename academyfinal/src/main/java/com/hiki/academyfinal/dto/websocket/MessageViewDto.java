package com.hiki.academyfinal.dto.websocket;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageViewDto {
	private Long messageNo;
	private String messageType;
	private String messageContent;
	private Timestamp messageTime;
	private String messageSender;
	private String messageReceiver;
	
	private String senderName;
	private String senderType;
	private String receiverName;
	private String receiverType;
}
