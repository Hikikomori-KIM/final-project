package com.hiki.academyfinal.vo.kakaopay;

import java.util.List;

import com.hiki.academyfinal.dto.kakaopay.PayDetailDto;
import com.hiki.academyfinal.dto.kakaopay.PayDto;

import lombok.Data;

//한 건의 결제에 대한 모든 정보가 다 담긴 클래스
@Data
public class PayTotalVO {
	private PayDto payDto;//결제대표정보
	private List<PayDetailDto> payList;//결제상세목록
}
