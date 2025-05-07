package com.hiki.academyfinal.vo.kakaopay;

import java.util.List;

import com.hiki.academyfinal.dto.kakaopay.BuyDetailDto;
import com.hiki.academyfinal.dto.kakaopay.BuyDto;

import lombok.Data;

//한 건의 결제에 대한 모든 정보가 다 담긴 클래스
@Data
public class BuyTotalVO {
	private BuyDto buyDto;//결제대표정보
	private List<BuyDetailDto> buyList;//결제상세목록
}
