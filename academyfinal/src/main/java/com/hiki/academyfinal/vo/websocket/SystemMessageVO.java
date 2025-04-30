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
	@Builder.Default
	private String type = "SYSTEM";
}
