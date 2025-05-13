package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ProductSalesVO {
    private Long productNo;
    private String productName;
    private String brand;
    private Integer discountedPrice;
    private Long totalSoldQty;
    private Long totalSalesAmount;
    private Long attachmentNo;
    private String imageUrl;
    private String strength;
    private int productPrice;
}
