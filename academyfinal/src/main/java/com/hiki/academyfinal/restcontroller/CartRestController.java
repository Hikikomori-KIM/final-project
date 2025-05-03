package com.hiki.academyfinal.restcontroller;

import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.CartDao;
import com.hiki.academyfinal.dao.ProductDetailViewDao;
import com.hiki.academyfinal.dto.CartDto;
import com.hiki.academyfinal.dto.ProductDetailViewDto;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartRestController {
	
	@Autowired
	private ProductDetailViewDao producvtDetailViewDao;
	
	@Autowired
	private CartDao cartDao;
	
	//다이얼로그에서 아이템정보띄우기
	@GetMapping("/{productNo}")
	public ProductDetailViewDto itemInfo(@PathVariable int productNo) {
		ProductDetailViewDto productDetailViewDto = producvtDetailViewDao.productInfo(productNo);
		return productDetailViewDto;
	}
	//카트추가
	@PostMapping("/insert")
	public ResponseEntity<String> createCart(CartDto cartDto) {
		//중복조회후 중복시 qtyUp / 아닐시 create
		cartDao.duplicationAndCreate(cartDto);
		return ResponseEntity.ok("가벼운 restcontroller^^");
	}
}
