package com.hiki.academyfinal.vo.kakaopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPaySelectedCardInfoVO {
	private String cardBin;//카드BIN
	private Integer installMonth;//할부 개월 수
	private String installmentType;//할부 유형
	private String cardCoprName;//카드사 정보
	private String interestFreeInstall;//무이자할부(Y/N)
}