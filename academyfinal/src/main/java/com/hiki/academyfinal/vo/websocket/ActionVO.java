package com.hiki.academyfinal.vo.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ActionVO {
	private String type; // enter, leave
	private String usersId;
}
