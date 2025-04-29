package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NaverUsersDto {
 
	private String naverUsersId;
	private String usersType;
	private String naverUsersName;
	private String naverUsersEmail;
	private String naverUsersMobile;
}
