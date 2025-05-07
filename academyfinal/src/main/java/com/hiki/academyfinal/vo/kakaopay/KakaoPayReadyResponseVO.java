package com.hiki.academyfinal.vo.kakaopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

//카카오에서 준비단계 완료 시 보내주는 데이터
//* 주의 : 자동으로 수신하려면 몇 가지 추가 설정이 필요 (jackson-databind)
@Data
//필드에 없는건 무시하세요!
@JsonIgnoreProperties(ignoreUnknown = true)
//JSON은 snake_case이니까 알아서 변환하세요
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayReadyResponseVO {
	private String tid;
	private String nextRedirectPcUrl;
	private String createdAt;
}