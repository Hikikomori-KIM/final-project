package com.hiki.academyfinal.vo.kakaopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPayCardInfoVO {
	private String kakaopayPurchaseCorp;//카카오페이 매입사명
	private String kakaopayPurchaseCorpCode;//카카오페이 매입사코드
	private String kakaopayIssuerCorp;//카카오페이 발급사명
	private String kakaopayIssuerCorpCode;//카카오페이 발급사코드
	private String bin;//카드 BIN
	private String cardType;//카드 타입
	private String installMonth;//할부 개월 수
	private String approvedId;//카드사 승인번호
	private String cardMid;//카드사 가맹점 번호
	private String interestFreeInstall;//무이자할부(Y/N)
	private String installmentType;//할부유형(업종무이자/분담무이자)
	private String cardItemCode;//카드 상품 코드
}