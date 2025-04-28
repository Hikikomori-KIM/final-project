package com.hiki.academyfinal.vo;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor @Component
public class UsersLoginResponseVO {
	private String usersId;
	private String usersType;
	private String accessToken;
	private String refreshToken;
}
