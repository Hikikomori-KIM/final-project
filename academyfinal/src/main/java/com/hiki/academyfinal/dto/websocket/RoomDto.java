package com.hiki.academyfinal.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RoomDto {
	private long roomNo;
	private String roomTitle;
	private String roomOwner;
}
