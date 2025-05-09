package com.hiki.academyfinal.vo;

import java.util.List;

import com.hiki.academyfinal.dto.ScentChoiceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMakeSurveyVO {
    private String scentQuestionContent;   // 클라이언트에서 보내는 scentQuestionContent 필드
    private List<ScentChoiceDto> scentChoiceDto; // scentChoiceDto 필드는 List<ScentChoiceDto>로 받음
    private int count; // count
}