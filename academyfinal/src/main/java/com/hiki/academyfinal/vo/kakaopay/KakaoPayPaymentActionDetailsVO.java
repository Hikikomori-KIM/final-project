package com.hiki.academyfinal.vo.kakaopay;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayPaymentActionDetailsVO {
	private String aid;//요청 고유번호
	private LocalDateTime approvedAt;//거래시간
	private Long amount;//결제/취소 총액
	private Long pointAmount;//결제/취소 포인트 금액
	private Long discountAmount;//할인 금액
	private Long greenDeposit;//컵 보증금
	private String paymentActionType;//결제타입(PAYMENT/CANCEL/ISSUED_SID)
	private String payload;//추가 전달한 텍스트
}