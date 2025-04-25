package com.hiki.academyfinal.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ProductsDto {
	private int productNo;             // VARCHAR2(20) → String
	private String productName;
	private String productDetail;
	private Integer productPrice;         // NUMBER → Integer
	private Integer productStock;
	private Timestamp productCreatedAt;

	private int categoryNo;            // FK 참조용
	private String brand;
	private String strength;
}
