package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class VolumeDto {
	private int volumeNo; //시퀀스
	private int productNo; // fk
	private String volumeMl; //ex: 50ml
	private int volumesStock; // 각 용량별 재고
}
