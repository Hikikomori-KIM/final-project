package com.hiki.academyfinal.vo.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPayCancelVO {
	private String tid;
	private long cancelAmount;
}