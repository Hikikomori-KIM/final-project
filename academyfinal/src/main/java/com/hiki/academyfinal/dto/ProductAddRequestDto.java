package com.hiki.academyfinal.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAddRequestDto {

    // 🔸 Products
    private String productName;
    private String productDetail;
    private Integer productPrice;
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
    private List<VolumeDto> volumes;
}
