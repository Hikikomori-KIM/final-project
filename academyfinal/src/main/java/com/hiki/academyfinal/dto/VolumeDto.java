package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class VolumeDto {
	private long volumeNo; // 시퀀스
	private long productNo; // FK
	private String volumeMl; // 예: 50ml
	private int volumeStock; // 각 용량별 재고
	private Integer volumePrice; // 원가
	private Integer discountedVolumePrice; // 할인 가격

	private String volumeStatus; // ✅ 상태값: active, inactive, deleted
	
	private String productName;
	private Integer productPrice;
}
