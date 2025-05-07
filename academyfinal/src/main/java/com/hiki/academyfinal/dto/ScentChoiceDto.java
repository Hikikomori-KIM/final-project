package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ScentChoiceDto {
	private int scentChoiceNo;
	private int scentQuestionNo;
	private String scentChoiceContent;
	private String scentChoiceType;
	private int scentChoiceScore;
	

}
