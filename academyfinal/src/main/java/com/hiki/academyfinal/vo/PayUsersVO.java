package com.hiki.academyfinal.vo;

import java.sql.Timestamp;

import com.hiki.academyfinal.dto.kakaopay.PayDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PayUsersVO {
	private long payNo;
	private String payOwner;
	private String payTid;
	private String payName;
	private long payTotal;
	private long payRemain;
	private Timestamp payTime;
	private String deliveryStatus;
	private String shipping;
	private String paymentMethod;
	private String payCash;
	
	private String usersName;
	private String usersProvider;
}
