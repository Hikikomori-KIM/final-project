package com.hiki.academyfinal.dto.kakaopay;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PayDto {
	private long payNo;
	private String payOwner;
	private String payTid;
	private String payName;
	private long payTotal;
	private long payRemain;
	private Timestamp payTime;
	private String deliveryStatus;
	private String shipping;
}
