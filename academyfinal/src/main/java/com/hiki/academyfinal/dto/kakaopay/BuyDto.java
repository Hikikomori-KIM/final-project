package com.hiki.academyfinal.dto.kakaopay;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BuyDto {
	private long buyNo;
	private String buyOwner;
	private String buyTid;
	private long buyToal;
	private long buyRemain;
	private Timestamp buyTime;
}
