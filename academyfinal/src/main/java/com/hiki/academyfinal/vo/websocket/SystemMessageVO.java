package com.hiki.academyfinal.vo.websocket;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SystemMessageVO implements MessageVO {
	private Long messageNo;
	private String content;
	private LocalDateTime time;
	// 모든 방에 동일하게 보여지는 메시지이므로 roomNo 필요 X
	@Builder.Default
	private String type = "SYSTEM";
}
