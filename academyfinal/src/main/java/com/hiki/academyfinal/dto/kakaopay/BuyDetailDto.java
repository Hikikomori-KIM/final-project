package com.hiki.academyfinal.dto.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BuyDetailDto {
	private long buyDetailNo; 
	private long buyDetailOrigin;
	private long buyDetailItem;
	private String buyDetailnName;
	private int buyDetailPrice;
	private int buyDetailQty;
	private String buyDetailStatus;
	public int getTotalPrice() {
		return buyDetailPrice * buyDetailQty;
	}
}
