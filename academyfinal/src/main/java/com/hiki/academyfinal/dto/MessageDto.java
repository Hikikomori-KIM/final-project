package com.hiki.academyfinal.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageDto {
	private Long messageNo; 
	private String messageType;
	// 회원→관리자 : 항상 앞에 관리자에게만 오도록 설정, hidden / admin에게 DM만 보낼 수 있음
	// 관리자→회원 : 회원 이름을 누르면 해당 회원에게 DM이 가도록 하는 구조
	private String messageContent; // 메시지 내용
	private Timestamp messageTime; // 메시지 전송 시각
	private String messageSender; // 메시지 보내는 사람 (회원 이름과 아이디 출력)(구분은 UsersId)
	private String messageReceiver; // 메시지 받는 사람 (only admin)
}
