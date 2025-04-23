package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UsersDto {
	
	private String usersId;
	private String usersName;
	private String usersPw;
	private String usersContact;
	private String usersBirth;
	private String usersType;
	private String usersEmail;
	

}
