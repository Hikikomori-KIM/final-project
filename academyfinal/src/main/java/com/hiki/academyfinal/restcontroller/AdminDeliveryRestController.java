package com.hiki.academyfinal.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.kakaopay.PayDao;
import com.hiki.academyfinal.dto.kakaopay.PayDto;
import com.hiki.academyfinal.service.KakaoPayService;

@RestController
@RequestMapping("/api/admin/pay")
@CrossOrigin
public class AdminDeliveryRestController {
	
	@Autowired
	private PayDao payDao;
	@Autowired
	private KakaoPayService kakaoPayService;
	
	@GetMapping("/all")
	public List<PayDto> getAllPayList() {
		return payDao.selectAllPayList();
	}
	
	@GetMapping("/detail/{payNo}")
	public Map<String, Object> getPayDetail(@PathVariable long payNo) {
	    Map<String, Object> result = new HashMap<>();
	    result.put("payInfo", payDao.selectOne(payNo));
	    result.put("productList", payDao.selectPayDetailList(payNo));
	    return result;
	}
	
	@DeleteMapping("/cancel/{payNo}")
	public void adminCancelPay(@PathVariable long payNo) {
	    payDao.cancelAll(payNo);
	}
	
	@PatchMapping("/shipping/{payNo}")
	public ResponseEntity<?> updateShippingStatus(
	    @PathVariable long payNo,
	    @RequestBody Map<String, String> body) {
	    String newStatus = body.get("newStatus");
	    boolean result = payDao.updateShippingStatus(payNo, newStatus);
	    return result ? ResponseEntity.ok().build()
	                  : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}

