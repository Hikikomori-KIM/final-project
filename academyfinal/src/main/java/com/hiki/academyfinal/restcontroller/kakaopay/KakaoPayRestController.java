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

import com.hiki.academyfinal.dao.PayDao;
import com.hiki.academyfinal.dao.ProductsDao;
import com.hiki.academyfinal.dao.VolumeDao;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.dto.VolumeDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.KakaoPayService;
import com.hiki.academyfinal.service.ProductService;
import com.hiki.academyfinal.service.TokenService;
import com.hiki.academyfinal.vo.ClaimVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayApproveResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayApproveVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayPayVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayReadyResponseVO;
import com.hiki.academyfinal.vo.kakaopay.KakaoPayReadyVO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/purchase")
public class KakaoPayRestController {

    @Autowired
	private ProductsDao productsDao;
	@Autowired
	private VolumeDao volumeDao;
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
	@PostMapping("/pay")
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
	
	@GetMapping("/pay/success/{partnerOrderId}")
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
	
	@GetMapping("/pay/cancel/{partnerOrderId}")
	public void cancel(@PathVariable String partnerOrderId, 
			HttpServletResponse response) throws IOException {
		flashMap.remove(partnerOrderId);
		payListMap.remove(partnerOrderId);
		readyMap.remove(partnerOrderId);
		
		String url = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(url+"/cancel");
	}
	
	@GetMapping("/pay/fail/{partnerOrderId}")
	public void fail(@PathVariable String partnerOrderId,
			HttpServletResponse response) throws IOException {
		flashMap.remove(partnerOrderId);
		payListMap.remove(partnerOrderId);
		readyMap.remove(partnerOrderId);
		
		String url = returnUrlMap.remove(partnerOrderId);
		response.sendRedirect(url+"/fail");
	}
}