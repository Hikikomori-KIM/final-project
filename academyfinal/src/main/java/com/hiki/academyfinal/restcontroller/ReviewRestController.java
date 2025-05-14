package com.hiki.academyfinal.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.AttachmentDao;
import com.hiki.academyfinal.dao.ReviewDao;
import com.hiki.academyfinal.dao.kakaopay.PayDao;
import com.hiki.academyfinal.dto.AttachmentDto;
import com.hiki.academyfinal.dto.ReviewDto;
import com.hiki.academyfinal.service.AttachmentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewRestController {

	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private PayDao payDao;
	 

	// 📌 특정 상품의 리뷰 목록 조회
	@GetMapping("/{productNo}")
	public List<ReviewDto> getReviews(@PathVariable long productNo) {
		return reviewDao.selectReviewList(productNo);
	}

	// 📌 리뷰 + 이미지 등록 (한 번에 처리)
	@PostMapping
	public void insertReview(
	    @RequestPart("dto") ReviewDto dto,
	    @RequestPart(value = "reviewImages", required = false) List<MultipartFile> files
	) throws IOException {

	    // ✅ Recoil에서 전달받은 값 그대로 사용 (임시 보안 허용)
	    String loginUserId = dto.getUsersId(); // 프론트에서 보낸 값

	    // ✅ null일 경우만 예외 처리
	    if (loginUserId == null || loginUserId.isBlank()) {
	        throw new IllegalStateException("로그인한 사용자만 리뷰 작성이 가능합니다.");
	    }

	    // ✅ 보안상 주의: 추후 서버 인증 도입 필요
	    boolean hasPurchased = payDao.hasUserPurchasedProduct(loginUserId, dto.getProductNo());
	    if (!hasPurchased) {
	        throw new IllegalStateException("리뷰는 구매한 상품에 대해서만 작성할 수 있습니다.");
	    }
	    
	    // ✅ 중복 리뷰 작성 확인
	    if (reviewDao.hasWrittenReview(loginUserId, dto.getProductNo())) {
	        throw new IllegalStateException("이미 해당 상품에 대한 리뷰를 작성하셨습니다.");
	    }
	    
	    reviewDao.insertReview(dto);
	    
	    if (files != null && !files.isEmpty()) {
	        for (MultipartFile file : files) {
	            long attachmentNo = attachmentDao.sequence();
	            AttachmentDto attachDto = AttachmentDto.builder()
	                .attachmentNo(attachmentNo)
	                .attachmentName(file.getOriginalFilename())
	                .attachmentType(file.getContentType())
	                .attachmentSize(file.getSize())
	                .build();
	            attachmentDao.insert(attachDto);
	            attachmentService.save(file, attachmentNo);
	            reviewDao.insertReviewImage(dto.getReviewNo(), attachmentNo);
	        }
	    }
	}



	@DeleteMapping("/admin/{reviewNo}")
	public void deleteReview(@PathVariable long reviewNo) {
		// 1. 리뷰 이미지 번호들 조회
		List<Long> attachmentNos = reviewDao.selectAttachmentNosByReviewNo(reviewNo);

		// 2. 실제 파일과 DB에서 첨부파일 삭제
		for (Long attachmentNo : attachmentNos) {
			attachmentService.remove(attachmentNo); // 로컬 파일 + attachment 테이블 삭제
		}

		// 3. 리뷰-이미지 연결 삭제
		reviewDao.deleteReviewImagesByReviewNo(reviewNo);

		// 4. 리뷰 삭제
		reviewDao.deleteReviewByReviewNo(reviewNo);
	}

	// 📌 관리자: 리뷰 삭제
	@DeleteMapping("/{reviewNo}")
	public void admindeleteReview(@PathVariable long reviewNo) {
		// 이미지 연동된 첨부파일도 삭제하려면 여기서 AttachmentService 호출 필요
		reviewDao.adminDeleteReview(reviewNo);
	}

}
