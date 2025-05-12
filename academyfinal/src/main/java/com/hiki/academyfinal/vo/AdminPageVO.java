package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
//이게 request //start end상속 ㅋㅋ굳이
public class AdminPageVO extends PageVO{
	private String usersType;
	private String usersId;
	private String usersName;
	private String usersEmail;
	private String usersProvider;

}
