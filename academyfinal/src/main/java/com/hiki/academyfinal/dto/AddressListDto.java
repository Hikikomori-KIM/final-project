package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AddressListDto {
	private long addressListNo;
	private String usersId;
	private String addressListPost;
	private String addressListBasic;
	private String addressListDetail;
	private String addressListDefault;

}
