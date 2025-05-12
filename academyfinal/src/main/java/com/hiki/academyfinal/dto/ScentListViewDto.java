package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ScentListViewDto {
	    private int scentQuestionNo;
	    private String scentQuestionContent;
	    private int scentChoiceNo;
	    private int choiceQuestionNo; // 선택지의 질문지번호
	    private String scentChoiceContent;
	    private String scentChoiceType;
	    private int scentChoiceScore;


}
