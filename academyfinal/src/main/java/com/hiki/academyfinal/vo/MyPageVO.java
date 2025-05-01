package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MyPageVO {
	private String usersName;
	private String usersContact;
	private String usersBirth;
	private String usersEmail;
	private String addressListBasic;
	private String addressListDetail;

}
