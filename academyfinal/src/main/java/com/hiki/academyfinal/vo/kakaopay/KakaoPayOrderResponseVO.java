package com.hiki.academyfinal.vo.kakaopay;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPayOrderResponseVO {
	private String tid;//결제 고유번호
	private String cid;//가맹점번호
	private String status;//결제상태
	private String partnerOrderId;//주문번호
	private String partnerUserId;//주문자
	private String paymentMethodType;//CARD 또는 MONEY
	private KakaoPayAmountVO amount;//결제금액
	private KakaoPayAmountVO canceledAmount;//취소된금액
	private KakaoPayAmountVO cancelAvailableAmount;//취소 가능 금액
	private String itemName;//상품명
	private String itemCode;//상품코드
	private int quantity;//구매수량(무조건1)
	private LocalDateTime createdAt;//준비시각
	private LocalDateTime approvedAt;//승인시각
	private LocalDateTime canceledAt;//취소시각
	private KakaoPaySelectedCardInfoVO selectedCardInfo;//결제카드정보
	//private KakaoPayPaymentActionDetails[] paymentActionDetails;
	private List<KakaoPayPaymentActionDetailsVO> paymentActionDetails;
}