package com.hiki.academyfinal.vo.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//카카오에게 준비단계에서 보낼 데이터
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPayReadyVO {
	private String partnerOrderId;
	private String partnerUserId;
	private String itemName;
	private long totalAmount;
}