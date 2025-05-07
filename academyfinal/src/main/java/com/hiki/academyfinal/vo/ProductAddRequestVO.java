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
public class ProductAddRequestVO {
    // 🔸 Products
    private String productName;
    private String productDetail;
    private Integer productPrice;
    private Integer discountRate;
    private Integer discountedPrice;
    private Integer brandNo;
    private String strength;

    // 🔸 PerfumeDetails
    private String topNote;
    private String middleNote;
    private String baseNote;

    // 🔸 추가: category 번호
    private Integer categoryNo;

    // 🔸 추가: 향 계열 accords 리스트
    private List<String> accords;

    // 🔸 Volume 리스트
    private List<VolumeInputVO> volumes;
    
    private String productDescriptionHtml; //html 상세 설명 저장용
}
