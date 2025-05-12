package com.hiki.academyfinal.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ReviewDto {
    private Long reviewNo;
    private Long productNo;
    private String usersId;
    private int reviewRating;
    private String reviewComment;
    private Timestamp reviewCreatedAt;
    private String attachmentNoList; // 이미지 여러개니까 콤마로 이어진 문자열

}

