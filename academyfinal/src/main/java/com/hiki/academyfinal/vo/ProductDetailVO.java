package com.hiki.academyfinal.vo;

import java.util.List;

import com.hiki.academyfinal.dto.VolumeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailVO {
    // product_detail_view에서 가져오는 기본 정보
    private long productNo;
    private String productName;
    private String productDetail;
    private int productPrice;
    private Integer discountRate;
    private int discountedPrice;
    private String strength;
    private String brandNo;
    private String categoryNo;
    private String brandName;
    private String topNote;
    private String middleNote;
    private String baseNote;

    // 이미지 정보 (attachment 기준)
    private String imageName;
    private String imageType;
    private long imageSize;

    // 향 계열 (accords)
    private List<String> accords;

    // 용량 및 재고 정보
    private List<VolumeDto> volumes;
    
    private String imageUrl; // url
    
    private String productDescriptionHtml;
}
