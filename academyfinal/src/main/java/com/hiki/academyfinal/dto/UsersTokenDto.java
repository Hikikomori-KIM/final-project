package com.hiki.academyfinal.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor 
public class UsersTokenDto {
	private Long usersTokenNo;
	private String usersTokenTarget;
	private String usersTokenValue;
	private Timestamp usersTokenTime;
}
