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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.ProductsDao;
import com.hiki.academyfinal.dao.VolumeDao;
import com.hiki.academyfinal.dao.kakaopay.PayDao;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.dto.VolumeDto;
import com.hiki.academyfinal.dto.kakaopay.PayDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.KakaoPayService;
import com.hiki.academyfinal.service.ProductService;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.DeliveryRequestVO;
import com.hiki.academyfinal.vo.DeliveryResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayApproveResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayApproveVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayCancelResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayCancelVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayPayVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayReadyResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayReadyVO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/purchase/pay")
public class KakaoPayRestController {

    @Autowired
	private ProductsDao productsDao;
	@Autowired
	private VolumeDao volumeDao;
	@Autowired
	private PayDao payDao;
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private TokenService tokenService;
	
	// Flash value를 저장하기 위한 저장소
	private Map<String, KakaoPayApproveVO> flashMap = Collections.synchronizedMap(new HashMap<>()); // thread-safe
	
	// 현재 거래번호가 완료되면 돌아갈 페이지 주소를 저장
	private Map<String, String> returnUrlMap = Collections.synchronizedMap(new HashMap<>()); //thread-safe
	
	// 상품번호+수량 목록을 저장
	private Map<String, List<KakaoPayPayVO>> payListMap = Collections.synchronizedMap(new HashMap<>()); //thread-safe
	
	// 결제준비 요청정보를 저장
	private Map<String, KakaoPayReadyVO> readyMap = Collections.synchronizedMap(new HashMap<>());

    KakaoPayRestController(ProductService productService) {
    } //thread-safe
	
	// 구매 요청 시 클라이언트가 {상품번호,수량}을 배열 형태로 전송
	// - 클래스를 만들어서 받을 수 있도록 준비해야한다
	@PostMapping
	public KakaoPayReadyResponseVO pay(
			@RequestBody List<KakaoPayPayVO> payList,
			@RequestHeader("Frontend-URL") String frontendUrl, 
			@RequestHeader("Authorization") String bearerToken) throws URISyntaxException {
		if(payList.isEmpty()) throw new TargetNotFoundException();
		
		KakaoPayReadyVO vo = new KakaoPayReadyVO();
		
		vo.setPartnerOrderId(UUID.randomUUID().toString());
		
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		vo.setPartnerUserId(claimVO.getUsersId());
		
		VolumeDto firstVolume = volumeDao.selectByVolumeNo(payList.get(0).getVolumeNo());
		if(firstVolume == null) throw new TargetNotFoundException("VolumeNo문제있삼");
	
		ProductsDto firstProduct = productsDao.selectOne(firstVolume.getProductNo());
		if(firstProduct == null) throw new TargetNotFoundException("없는 상품");
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(firstProduct.getProductName());
		if(payList.size() >= 2) {
			buffer.append(" 외 " + (payList.size()-1) + "건");
		}
		vo.setProductName(buffer.toString());
		
		int totalAmount = 0;
		for(KakaoPayPayVO payVO : payList) {
		    VolumeDto volumeDto = volumeDao.selectByVolumeNo(payVO.getVolumeNo());
		    if (volumeDto != null) {
		        totalAmount += volumeDto.getDiscountedVolumePrice() * payVO.getQty();
		    }
		}
		vo.setTotalAmount(totalAmount);
		
		KakaoPayReadyResponseVO response = kakaoPayService.ready(vo);
		
		// 다음 매핑을 위한 추가 정보를 저장
		flashMap.put(vo.getPartnerOrderId(), KakaoPayApproveVO.builder()
					.partnerOrderId(vo.getPartnerOrderId())
					.partnerUserId(vo.getPartnerUserId())
					.tid(response.getTid())
				.build());
		returnUrlMap.put(vo.getPartnerOrderId(), frontendUrl);
		payListMap.put(vo.getPartnerOrderId(), payList);
		readyMap.put(vo.getPartnerOrderId(), vo);
		
		return response;
	}
	
	@GetMapping("/success/{partnerOrderId}")
	public void success(@PathVariable String partnerOrderId,
						@RequestParam("pg_token") String pgToken,
						HttpServletResponse response) throws URISyntaxException, IOException {
		KakaoPayApproveVO vo = flashMap.remove(partnerOrderId);
		if(vo == null) throw new TargetNotFoundException("유효하지 않은 결제 정보");
		
		vo.setPgToken(pgToken);
	
		KakaoPayApproveResponseVO approveResponse = kakaoPayService.approve(vo);
		log.debug("approve = {}", approveResponse);
		
		// DB에 결제완료된 정보를 저장하는 처리를 추가
		// - 저장하기 위해서는 상품번호와 상품수량이 담긴 목록이 필요
		// - ready 시점에서 존재하는 데이터이므로 flash data로 저장해서 이동
		List<KakaoPayPayVO> payList = payListMap.remove(partnerOrderId);
		KakaoPayReadyVO readyVO = readyMap.remove(partnerOrderId);
		
		// DB등록
		kakaoPayService.insertDB(vo, readyVO, payList);
		
		// react로 리다이렉트 처리
		String returnUrl = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(returnUrl + "/success");
	}
	
	@GetMapping("/cancel/{partnerOrderId}")
	public void cancel(@PathVariable String partnerOrderId, 
			HttpServletResponse response) throws IOException {
		flashMap.remove(partnerOrderId);
		payListMap.remove(partnerOrderId);
		readyMap.remove(partnerOrderId);
		
		String url = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(url+"/cancel");
	}
	
	@GetMapping("/fail/{partnerOrderId}")
	public void fail(@PathVariable String partnerOrderId,
			HttpServletResponse response) throws IOException {
		flashMap.remove(partnerOrderId);
		payListMap.remove(partnerOrderId);
		readyMap.remove(partnerOrderId);
		
		String url = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(url+"/fail");
	}
	
	// 배송 과정 리스트 조회 (관리자용)
	// * List<Map<String, Object>>
	// - List : 여러 개의 데이터를 "순서대로 모은" 컬렉션 (배열과 유사)
	// - Map<String, Object> : 한개의 row(행)을 컬럼명-값 형태로 표현
	// : 굳이 DTO 클래스를 만들지 않아도 괜찮음
	// : 컬럼 이름이 고정되어 있지 않거나 간단한 admin 페이지에서는 편리함
	// : 유연하게 컬럼 추가 및 삭제가 가능함
	// : react에서 axios로 받으면 JSON 형태로 오기 때문에 .map() 돌려서 사용 가능
	// : 단, 타입 캐스팅이나 안정성 측면에서는 dto보다 덜 안전함
	@GetMapping("/delivery-list")
	public List<Map<String, Object>> getDeliveryList() {
		return payDao.findDeliveryList();
	}
	
	// 구매 내역 조회
	@GetMapping("/listPay/{usersId}")
	public List<PayDto> getPurchaseList(@PathVariable String usersId) {
		return payDao.getPurchaseListByUserId(usersId);
	}
	
	// 구매 상세 조회
	@GetMapping("/listPay/detail/{payNo}")
	public Map<String, Object> getPayDetail(@PathVariable long payNo) {
	    Map<String, Object> result = new HashMap<>();
	    result.put("payInfo", payDao.selectOne(payNo));
	    result.put("productList", payDao.selectPayDetailList(payNo));
	    return result;
	}
//	@GetMapping("/listPay/detail/{payNo}")
//	public KakaoPayOrderResponseVO order(
//			@PathVariable long payNo,
//			@RequestHeader("Authorization") String bearerToken) throws URISyntaxException {
//		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
//		PayDto payDto = payDao.selectOne(payNo);
//		if(claimVO.getUsersId().equals(payDto.getPayOwner()) == false)
//			throw new TargetNotFoundException();
//		return kakaoPayService.order(KakaoPayOrderVO.builder()
//					.tid(payDto.getPayTid())
//				.build());
//	}
	
	// 결제 취소하기 (전체취소)
	@DeleteMapping("/cancelAll/{payNo}")
	public void cancelAll(
			@RequestHeader("Authorization") String bearerToken,
			@PathVariable long payNo) throws URISyntaxException {
		// 토큰 해석
		ClaimVO claimVO = tokenService.parseBearerToken(bearerToken);
		// 상세 조회
		PayDto payDto = payDao.selectOne(payNo);
		// 결제 내역이 없을 경우
		if(payDto == null) throw new TargetNotFoundException();
		// 본인 확인
		if(payDto.getPayOwner().equals(claimVO.getUsersId()) == false)
			throw new TargetNotFoundException();
		// 카카오페이 취소 요청
		KakaoPayCancelResponseVO response = kakaoPayService.cancel(
				KakaoPayCancelVO.builder()
					.tid(payDto.getPayTid())
					.cancelAmount(payDto.getPayTotal())
				.build()
		);
		payDao.updatePay(payNo, 0L);
		payDao.cancelAll(payNo);
		payDao.updateDeliveryStatus(payNo, "결제 취소");
	}


	
	
	//전체결제관리 ㅇㅇㅇㅇㅇㅇㅇㅇ
	@PostMapping("/total")
	public DeliveryResponseVO totalList(@RequestBody DeliveryRequestVO deliveryRequestVO) {
		return payDao.payAllList(deliveryRequestVO);
	}
}