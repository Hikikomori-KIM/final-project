package com.hiki.academyfinal.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CartDto {
	private Long cartItemsNo; // 장바구니 아이템번호
	private Long productNo; // 상품번호
	private String usersId; // 사용자 아이디
	private Long cartQuantity; // 수량 (상품 재고 연결)
	private Timestamp cartCreatedtAt; // 장바구니 시간
}
