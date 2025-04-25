package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FaqDto {
	private long faqNo;
	private String faqType;
	private String faqTitle;
	private String faqContent;
}
