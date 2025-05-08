package com.hiki.academyfinal.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ProductsDto { 
    private long productNo;             
    private String productName;
    private String productDetail;
    private Integer productPrice;
    private Integer discountRate;
    private Integer discountedPrice;
    private Timestamp productCreatedAt;
    

    private int categoryNo;           // FK
    private int brandNo;              // ✅ 브랜드 이름(X) → 브랜드 번호(FK)
    private String strength;
    
    private Integer attachmentNo; // ✅ 추가
    
    private String imageUrl; //이미지 경로
    
    private String productDescriptionHtml; //html 상세 설명 저장용
    
}
