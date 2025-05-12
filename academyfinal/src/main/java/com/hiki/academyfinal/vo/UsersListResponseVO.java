package com.hiki.academyfinal.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UsersListResponseVO {
	private int count;
	private List<ManagementResponseVO>usersList;

}
