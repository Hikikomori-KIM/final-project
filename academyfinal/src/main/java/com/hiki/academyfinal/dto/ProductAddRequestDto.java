package com.hiki.academyfinal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductAddRequestDto { // 상품입력시 한방에 처리하려는 dto
	 // 🔸 Products
    private String productName;
    private String productDetail;
    private Integer productPrice;
    private String brand;
    private String strength;

    // 🔸 PerfumeDetails
    private String topNote;
    private String middleNote;
    private String baseNote;

    // 🔸 Volume 리스트 (ex: [{volumeMl: \"50ml\", volumeStock: 10}, ...])
    private List<VolumeDto> volumes;
}
