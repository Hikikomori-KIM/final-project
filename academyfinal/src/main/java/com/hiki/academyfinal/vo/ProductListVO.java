package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListVO {
    private long productNo;
    private String productName;
    private int productPrice;
    private int discountRate;
    private int discountedPrice;
    private String strength;
    private String brand;
    private Integer attachmentNo; // ✅ 꼭 필요함!
    private String imageUrl;
    private String mdPick; // "Y" 또는 "N"
}
