package com.hiki.academyfinal.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DeliveryResponseVO{
	//총금액떄메 하나더팜 개열받네
	private List<PayUsersVO> list;
	//춍금액 ㅡㅅㅡ
	private Long totalPrice; 
	private int count;
	//추가
	
}
