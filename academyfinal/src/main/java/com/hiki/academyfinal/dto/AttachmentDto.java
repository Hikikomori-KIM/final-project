package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttachmentDto {
	private long attachmentNo;		//시퀀스
	private String attachmentName;	//원본 파일명
	private String attachmentType;	//파일 타입
	private Long attachmentSize;		//바이트 단위 크기
}
