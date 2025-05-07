package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CartViewVO extends PageVO{
	//cart
	private Long cartNo;
	private Long cartItemNo;
	private String usersId;
	private int cartQty;	//default 1설정해놔서 Integer안씀
	private Long volumeNo;
	
	//productView
	private long productNo;
	private String productName;
	private String productDetail;
	private int productPrice;
	private int discountRate;
	private int discountedPrice;
	private String strength;
	private long brandNo;
	private long categoryNo;
	private String productDescriptionHtml;
	private String brandName;
	private String topNote;
	private String middleNote;
	private String baseNote;
	private String attachmentName;
	private String attachmentType;
	private String attachmentSize;
	private String imageUrl;
	private String volumeMl;
	private int volumeStock;
	private int volumePrice;
}
