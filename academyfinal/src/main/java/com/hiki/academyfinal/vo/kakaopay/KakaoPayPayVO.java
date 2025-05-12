package com.hiki.academyfinal.vo.kakaopay;
import lombok.Data;

@Data
public class KakaoPayPayVO {
    private long productNo;  // 상품 번호
    private long volumeNo;   // ✅ 용량 번호 (추가)
    private int qty;         // 수량
}
