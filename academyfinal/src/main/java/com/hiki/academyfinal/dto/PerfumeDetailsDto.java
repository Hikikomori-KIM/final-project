package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class PerfumeDetailsDto {
	private long perfumeNo;  //시퀀스
	private long productNo;  //fk
	private String topNote;
	private String middleNote;
	private String baseNote;
	private String strengthLevel; // 중복 가능성 있으나 별도로 관리할 수도
}
