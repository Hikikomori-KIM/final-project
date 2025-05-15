package com.hiki.academyfinal.dto.kakaopay;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PurchaseDetailViewDto {
	// payDto
	private long payNo; // 주문번호
	private String payOwner; // 주문자
	private long payTotal; // 총 가격
	private Timestamp payTime; // 주문일자
	private String deliveryStatus; // 결제 상태
	private String shipping; // 배송진행 상태
	
	// payDetailDto
	private long payDetailOrigin; // payDetailDto에서의 주문번호(=payNo)
	private String payDetailName; // 각각의 상품명
	private long payDetailPrice; // 각각의 상품 가격
	private long productNo; // 각각의 상품 번호
	private String payDetailStatus; // 결제 상태(Y, N)
}
