package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NaverLoginInfoVO {
 private String code;
 private String state;
}
