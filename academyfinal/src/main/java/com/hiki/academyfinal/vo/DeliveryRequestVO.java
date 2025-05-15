package com.hiki.academyfinal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DeliveryRequestVO extends PageVO{
	//start end는 받아오고 
	//여기는 딜리버리 복합검색
	private boolean today; //오늘선택
	private boolean dayOne; //1일전선택
	private boolean week; //1주일전 선택
	private boolean month; //1달 선택
	private boolean deliveryWait; //배송준비중인상품
	private boolean deliveryOn; //배송중인상품
	private boolean deliveryComplete; //배송완료
	private boolean returnProduct; //반품준비
	private boolean returnComplete; //반품완료
	private String column; //컬럼명 (뭐할지는 모르겠음 스네이크케이스로전달)
	private String keyword; //키워드
	
}
