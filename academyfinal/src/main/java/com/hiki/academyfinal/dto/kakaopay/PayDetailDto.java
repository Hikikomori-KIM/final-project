package com.hiki.academyfinal.dto.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PayDetailDto {

	private long payDetailNo;         // 기본키
	private long payDetailOrigin;     // 결제 번호 (pay_no)
	private long productNo;           // 상품 번호 (products 테이블 참조)
	private long volumeNo;            // 용량 번호 (volume 테이블 참조)

	private String payDetailName;     // 표시용 상품명 ("샤넬 알뤼르 스포츠 30ml" 등)
	private int payDetailPrice;       // 단가
	private int payDetailQty;         // 수량
	private String payDetailStatus;   // 상태 (Y/N)

	public int getTotalPrice() {
		return payDetailPrice * payDetailQty;
	}
}
