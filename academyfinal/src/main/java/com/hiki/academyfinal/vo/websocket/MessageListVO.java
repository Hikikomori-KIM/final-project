package com.hiki.academyfinal.vo.websocket;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageListVO {
	private boolean last;
	private List<MessageVO> list;
}
