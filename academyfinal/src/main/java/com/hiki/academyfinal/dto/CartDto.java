package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CartDto {
	private Long cartNo;
	private Long cartItemNo;
	private String usersId;
	private int cartQty;	//default 1설정해놔서 Integer안씀
}
