package com.hiki.academyfinal.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoticeDto {
	private long noticeNo;
	private String noticeTitle;
	private String noticeContent;
	private Timestamp noticeDate;

}
