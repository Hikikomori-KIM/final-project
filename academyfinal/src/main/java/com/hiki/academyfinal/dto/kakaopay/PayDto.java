package com.hiki.academyfinal.dto.kakaopay;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PayDto {
	private long payNo;
	private String payOwner;
	private String payTid;
	private String payName;
	private long payTotal;
	private long payRemain;
	private Timestamp payTime;
	private String deliveryStatus;
	private String shipping;
	// 배송진행 상태(배송준비, 배송중, 배송완료, 반품준비, 반품완료)
	private String paymentMethod;
	// 결제방법(card, cash) / 카드결제, 현금결제
	private String payCash;
	// 계좌이체 했을 경우(입금대기중, 입금확인)
}
