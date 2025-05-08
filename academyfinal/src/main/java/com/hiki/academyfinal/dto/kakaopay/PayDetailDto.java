package com.hiki.academyfinal.dto.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PayDetailDto {
	private long payDetailNo; 
	private long payDetailOrigin;
	private long payDetailItem;
	private String payDetailName;
	private int payDetailPrice;
	private int payDetailQty;
	private String payDetailStatus;
	public int getTotalPrice() {
		return payDetailPrice * payDetailQty;
	}
}
