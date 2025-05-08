package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ScentRecommendationDto {
	private int scentRecommendationNo;
	private int productNo;
	private String scentRecommendationComment;
	private String topScoreType;
}
