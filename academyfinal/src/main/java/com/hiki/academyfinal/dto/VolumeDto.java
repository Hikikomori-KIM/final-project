package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class VolumeDto {
	private long volumeNo; //시퀀스
	private long productNo; // fk
	private String volumeMl; //ex: 50ml
	private int volumeStock; // 각 용량별 재고
	private Integer volumePrice; // 추가\
	private Integer discountedVolumePrice;
}
