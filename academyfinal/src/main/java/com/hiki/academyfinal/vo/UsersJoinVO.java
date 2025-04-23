package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UsersJoinVO {

	private String usersId;
	private String usersName;
	private String usersPw;
	private String usersContact;
	private String usersBirth;
	private String usersEmail;

}
