package com.hiki.academyfinal.dto.kakaopay;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PurchaseDetailViewDto {
	// payDto
	private long payNo;
	private String payOwner;
	private String payTid;
	private String payName;
	private long payTotal;
	private long payRemain;
	private Timestamp payTime;
	private String deliveryStatus;
	private String shipping;
	private String paymentMethod;
	private String payCash;
	
	// payDetailDto
	private long payDetailOrigin; // payDetailDto에서의 주문번호(=payNo)
	private String payDetailName; // 각각의 상품명
	private long payDetailPrice; // 각각의 상품 가격
	private long productNo; // 각각의 상품 번호
	private String payDetailStatus; // 결제 상태(Y, N)
	private long payDetailNo;         // 기본키
	private long volumeNo;            // 용량 번호 (volume 테이블 참조)
	private int payDetailQty;         // 수량
}
