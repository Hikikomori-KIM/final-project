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

	//productView
	private long productNo;
	private String productName;
	private String productDetail;
	private int productPrice;
	private String strength;
	private long brandNo;
	private long categoryNo;
	private String productDescriptionHtml;
	private String brandName;
	private String topNote;
	private String middleNote;
	private String baseNote;
	private String imageName;
	private String imageType;
	private String imageSize;
	private String imageUrl;
	
	//볼륨
	private int volumeNo; //시퀀스
	private String volumeMl; //ex: 50ml
	private int volumeStock; // 각 용량별 재고

}
