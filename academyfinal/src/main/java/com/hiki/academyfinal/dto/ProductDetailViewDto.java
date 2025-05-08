package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ProductDetailViewDto {
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
	private int volumeNo;
	private String volumeMl;
	private int volumeStock;
	private int volumePrice;
	private int discountedVolumePrice;

}
