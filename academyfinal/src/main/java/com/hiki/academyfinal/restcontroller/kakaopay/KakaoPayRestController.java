package com.hiki.academyfinal.restcontroller.kakaopay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.BuyDao;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.KakaoPayService;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayApproveResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayApproveVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayBuyVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayReadyResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayReadyVO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/kakaopay/v2")
public class KakaoPayRestController {
//	@Autowired
//	private ItemDao itemDao; 상품 dao autowired 필요
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private BuyDao buyDao;
	
	//Flash value를 저장하기 위한 저장소
	private Map<String, KakaoPayApproveVO> flashMap = 
			Collections.synchronizedMap(new HashMap<>());//thread-safe
	
	//현재 거래번호가 완료되면 돌아갈 페이지 주소를 저장
	private Map<String, String> returnUrlMap = 
			Collections.synchronizedMap(new HashMap<>());//thread-safe
	
	//상품번호+수량 목록을 저장
	private Map<String, List<KakaoPayBuyVO>> buyListMap = 
			Collections.synchronizedMap(new HashMap<>());//thread-safe
	
	//결제준비 요청정보를 저장
	private Map<String, KakaoPayReadyVO> readyMap = 
			Collections.synchronizedMap(new HashMap<>());//thread-safe
	
	//구매 요청 시 클라이언트가 {상품번호,수량}을 배열 형태로 전송
	//- 클래스를 만들어서 받을 수 있도록 준비해야한다
	@PostMapping("/buy")
	public KakaoPayReadyResponseVO buy(
			@RequestBody List<KakaoPayBuyVO> buyList,
			@RequestHeader("Frontend-URL") String frontendUrl, 
			@RequestHeader("Authorization") String bearerToken) throws URISyntaxException {
		if(buyList.isEmpty()) throw new TargetNotFoundException();
		
		KakaoPayReadyVO vo = new KakaoPayReadyVO();
		
		vo.setPartnerOrderId(UUID.randomUUID().toString());
		
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		vo.setPartnerUserId(claimVO.getUsersId());
		
		//결제에 필요한 정보를 계산 (itemName, totalAmount)
		//- 상품명(itemName) : 상품이 1개일때는 해당상품명, 2개이상이면 ???외 ?건
		StringBuffer buffer = new StringBuffer();		
//		ItemDto firstItemDto = itemDao.selectOne(buyList.get(0).getItemNo());
//		buffer.append(firstItemDto.getItemName());
//		if(buyList.size() >= 2) {
//			buffer.append(" 외 "+(buyList.size()-1)+"건");
//		}
		vo.setItemName(buffer.toString());
		
		//- 합계(totalAmount) : buyList의 모든 항목을 조회하여 할인금액 * 수량
		int totalAmount = 0;
		for(KakaoPayBuyVO buyVO : buyList) {
//			ItemDto itemDto = itemDao.selectOne(buyVO.getItemNo());//상품정보 구하기
//			totalAmount += itemDto.getRealPrice() * buyVO.getQty();
		}
		vo.setTotalAmount(totalAmount);
		
		KakaoPayReadyResponseVO response = kakaoPayService.ready(vo);
		
		//다음 매핑을 위한 추가 정보를 저장
		flashMap.put(vo.getPartnerOrderId(), KakaoPayApproveVO.builder()
					.partnerOrderId(vo.getPartnerOrderId())
					.partnerUserId(vo.getPartnerUserId())
					.tid(response.getTid())
				.build());
		returnUrlMap.put(vo.getPartnerOrderId(), frontendUrl);
		buyListMap.put(vo.getPartnerOrderId(), buyList);
		readyMap.put(vo.getPartnerOrderId(), vo);
		
		return response;
	}
	
	@GetMapping("/buy/success/{partnerOrderId}")
	public void success(@PathVariable String partnerOrderId,
								@RequestParam("pg_token") String pgToken,
								HttpServletResponse response) throws URISyntaxException, IOException {
		KakaoPayApproveVO vo = flashMap.remove(partnerOrderId);
		if(vo == null) throw new TargetNotFoundException("유효하지 않은 결제 정보");
		
		vo.setPgToken(pgToken);
	
		KakaoPayApproveResponseVO approveResponse = 
														kakaoPayService.approve(vo);
		log.debug("approve = {}", approveResponse);
		
		//DB에 결제완료된 정보를 저장하는 처리를 추가
		//- 저장하기 위해서는 상품번호와 상품수량이 담긴 목록이 필요
		//- ready 시점에서 존재하는 데이터이므로 flash data로 저장해서 이동
		List<KakaoPayBuyVO> buyList = buyListMap.remove(partnerOrderId);
		KakaoPayReadyVO readyVO = readyMap.remove(partnerOrderId);
		
		//DB등록
		kakaoPayService.insertDB(vo, readyVO, buyList);
		
		//react로 리다이렉트 처리
		String returnUrl = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(returnUrl + "/success");
	}
	
	@GetMapping("/buy/cancel/{partnerOrderId}")
	public void cancel(@PathVariable String partnerOrderId,
								HttpServletResponse response) throws IOException {
		flashMap.remove(partnerOrderId);
		buyListMap.remove(partnerOrderId);
		readyMap.remove(partnerOrderId);
		
		String url = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(url+"/cancel");
	}
	
	@GetMapping("/buy/fail/{partnerOrderId}")
	public void fail(@PathVariable String partnerOrderId,
			HttpServletResponse response) throws IOException {
		flashMap.remove(partnerOrderId);
		buyListMap.remove(partnerOrderId);
		readyMap.remove(partnerOrderId);
		
		String url = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(url+"/fail");
	}
}