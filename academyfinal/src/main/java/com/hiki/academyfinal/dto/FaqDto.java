package com.hiki.academyfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FaqDto {
	private long faq_no;
	private String faq_type;
	private String faq_title;
	private String faq_content;
}
