package com.hiki.academyfinal.vo.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//카카오페이에 승인을 요청할 때 필요한 정보가 담긴 VO
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPayApproveVO {
	private String tid;//거래번호
	private String partnerOrderId;//주문번호
	private String partnerUserId;//구매자ID
	private String pgToken;//유효성 검증용 토큰
}