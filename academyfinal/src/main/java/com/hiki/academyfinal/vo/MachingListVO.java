package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class MachingListVO {
	private int scentRecommendationNo;
	private long productNo;
	private String scentRecommendationComment;
	private String topScoreType;
	private String productName;
}
