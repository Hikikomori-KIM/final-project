package com.hiki.academyfinal.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.AttachmentDao;
import com.hiki.academyfinal.dao.ReviewDao;
import com.hiki.academyfinal.dto.AttachmentDto;
import com.hiki.academyfinal.dto.ReviewDto;
import com.hiki.academyfinal.service.AttachmentService;

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

	// 📌 특정 상품의 리뷰 목록 조회
	@GetMapping("/{productNo}")
	public List<ReviewDto> getReviews(@PathVariable long productNo) {
		return reviewDao.selectReviewList(productNo);
	}

	// 📌 리뷰 + 이미지 등록 (한 번에 처리)
	@PostMapping
	public void insertReview(@RequestPart("dto") ReviewDto dto,
			@RequestPart(value = "reviewImages", required = false) List<MultipartFile> files) throws IOException {
		// 1. 리뷰 먼저 등록 (reviewNo가 dto에 담김)
		reviewDao.insertReview(dto);

		// 2. 이미지가 있으면 처리
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				// 2-1. 시퀀스 번호 먼저 조회
				long attachmentNo = attachmentDao.sequence();

				// 2-2. DB에 메타데이터 저장
				AttachmentDto attachDto = AttachmentDto.builder().attachmentNo(attachmentNo)
						.attachmentName(file.getOriginalFilename()).attachmentType(file.getContentType())
						.attachmentSize(file.getSize()).build();
				attachmentDao.insert(attachDto);

				// 2-3. 실제 파일 저장 (로컬 디렉토리에)
				attachmentService.save(file, attachmentNo);

				// 2-4. 리뷰 이미지 연결 테이블 등록
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
