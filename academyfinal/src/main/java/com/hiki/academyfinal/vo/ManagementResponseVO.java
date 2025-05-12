package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
//하 response할 유저데이터
public class ManagementResponseVO {

	private String usersId;
	private String usersName;
	private String usersContact;
	private String usersBirth;
	private String usersEmail;
	private String usersProvider;

}
