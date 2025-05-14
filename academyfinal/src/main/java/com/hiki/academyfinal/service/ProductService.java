package com.hiki.academyfinal.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.*;
import com.hiki.academyfinal.dto.*;
import com.hiki.academyfinal.vo.ProductAddRequestVO;
import com.hiki.academyfinal.vo.ProductListVO;
import com.hiki.academyfinal.vo.VolumeInputVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private ProductsDao productsDao;
	@Autowired
	private PerfumeDetailsDao perfumeDetailsDao;
	@Autowired
	private VolumeDao volumeDao;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AccordDao accordDao;
	@Autowired
	private ProductAccordDao productAccordDao;
	@Autowired
	private ReviewsDao reviewsDao;
	@Autowired
	private CartDao cartDao;

	// ✅ 상품 등록
	@Transactional(rollbackFor = Exception.class)
	public void registerProduct(ProductAddRequestVO VO, List<MultipartFile> images, List<String> types)
			throws IOException {
		long productNo = productsDao.sequence();

		int minPrice = VO.getVolumes().stream().mapToInt(VolumeInputVO::getVolumePrice).min()
				.orElseThrow(() -> new IllegalArgumentException("용량 가격이 비어 있습니다."));
		int discountRate = VO.getDiscountRate();
		int discountedPrice = discountRate > 0 ? (int) Math.floor(minPrice * (100 - discountRate) / 100.0) : minPrice;

		ProductsDto productsDto = ProductsDto.builder().productNo(productNo).productName(VO.getProductName())
				.productDetail(VO.getProductDetail()).productPrice(minPrice).discountRate(discountRate)
				.discountedPrice(discountedPrice).brandNo(VO.getBrandNo()).strength(VO.getStrength())
				.categoryNo(VO.getCategoryNo()).productDescriptionHtml(VO.getProductDescriptionHtml()).build();
		productsDao.insert(productsDto);

		long perfumeNo = perfumeDetailsDao.sequence();
		PerfumeDetailsDto perfumeDto = PerfumeDetailsDto.builder().perfumeNo(perfumeNo).productNo(productNo)
				.topNote(VO.getTopNote()).middleNote(VO.getMiddleNote()).baseNote(VO.getBaseNote()).build();
		perfumeDetailsDao.insert(perfumeDto);

		for (VolumeInputVO input : VO.getVolumes()) {
			long volumeNo = volumeDao.sequence();
			int originPrice = input.getVolumePrice();
			int discounted = discountRate > 0 ? (originPrice * (100 - discountRate) / 100) : originPrice;

			VolumeDto volume = VolumeDto.builder().volumeNo(volumeNo).productNo(productNo).volumeMl(input.getVolumeMl())
					.volumeStock(input.getVolumeStock()).volumePrice(originPrice).discountedVolumePrice(discounted)
					.build();
			volumeDao.insert(volume);
		}

		if (images != null && types != null && images.size() == types.size()) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile file = images.get(i);
				String type = types.get(i);
				if (file != null && !file.isEmpty()) {
					long attachNo = attachmentDao.sequence();
					AttachmentDto attachDto = AttachmentDto.builder().attachmentNo(attachNo)
							.attachmentName(file.getOriginalFilename()).attachmentType(file.getContentType())
							.attachmentSize(file.getSize()).build();
					attachmentDao.insert(attachDto);
					attachmentService.save(file, attachNo);
					productImgDao.insert(productNo, attachNo, type);
				}
			}
		}

		if (VO.getAccords() != null && !VO.getAccords().isEmpty()) {
			for (String accordName : VO.getAccords()) {
				Integer accordNo = accordDao.findAccordNoByName(accordName);
				if (accordNo != null) {
					productAccordDao.insert(new ProductAccordDto(productNo, accordNo));
				}
			}
		}
	}

	// ✅ 상품 수정
	@Transactional
	public void updateProduct(long productNo, ProductAddRequestVO VO, MultipartFile image) throws IOException {
		int minPrice = VO.getVolumes().stream().mapToInt(VolumeInputVO::getVolumePrice).min()
				.orElseThrow(() -> new IllegalArgumentException("용량 가격이 비어 있습니다."));
		int discountRate = VO.getDiscountRate();
		int discountedPrice = discountRate > 0 ? (int) Math.floor(minPrice * (100 - discountRate) / 100.0) : minPrice;

		// 1. 상품 기본 정보 업데이트
		ProductsDto productsDto = ProductsDto.builder().productNo(productNo).productName(VO.getProductName())
				.productDetail(VO.getProductDetail()).productPrice(minPrice).discountRate(discountRate)
				.discountedPrice(discountedPrice).brandNo(VO.getBrandNo()).strength(VO.getStrength())
				.categoryNo(VO.getCategoryNo()).productDescriptionHtml(VO.getProductDescriptionHtml()).build();
		productsDao.update(productsDto);

		// 2. 향 정보 삭제 후 재삽입
		perfumeDetailsDao.deleteByProductNo(productNo);
		long perfumeNo = perfumeDetailsDao.sequence();
		PerfumeDetailsDto perfumeDto = PerfumeDetailsDto.builder().perfumeNo(perfumeNo).productNo(productNo)
				.topNote(VO.getTopNote()).middleNote(VO.getMiddleNote()).baseNote(VO.getBaseNote()).build();
		perfumeDetailsDao.insert(perfumeDto);

		// ✅ 3. 기존 용량 전부 소프트 삭제 처리
		volumeDao.markDeletedByProductNo(productNo);

		// ✅ 4. 수정 요청된 용량 처리
		for (VolumeInputVO input : VO.getVolumes()) {
			VolumeDto existing = volumeDao.findByProductNoAndMl(productNo, input.getVolumeMl());

			int originPrice = input.getVolumePrice();
			int discounted = discountRate > 0 ? (originPrice * (100 - discountRate) / 100) : originPrice;

			if (existing != null) {
				// 기존 용량 → 수정 + 상태 복구
				existing.setVolumeStock(input.getVolumeStock());
				existing.setVolumePrice(originPrice);
				existing.setDiscountedVolumePrice(discounted);
				existing.setVolumeStatus("active");
				volumeDao.updateVolume(existing);
				volumeDao.updateVolumeStatus(existing.getVolumeNo(), "active");
			} else {
				// 신규 용량 → 추가
				long volumeNo = volumeDao.sequence();
				VolumeDto volume = VolumeDto.builder().volumeNo(volumeNo).productNo(productNo)
						.volumeMl(input.getVolumeMl()).volumeStock(input.getVolumeStock()).volumePrice(originPrice)
						.discountedVolumePrice(discounted).volumeStatus("active").build();
				volumeDao.insert(volume);
			}
		}

		// 5. 이미지 수정
		if (image != null && !image.isEmpty()) {
			productImgDao.deleteByProductNo(productNo);
			long attachNo = attachmentDao.sequence();
			AttachmentDto attachDto = AttachmentDto.builder().attachmentNo(attachNo)
					.attachmentName(image.getOriginalFilename()).attachmentType(image.getContentType())
					.attachmentSize(image.getSize()).build();
			attachmentDao.insert(attachDto);
			attachmentService.save(image, attachNo);
			productImgDao.insert(productNo, attachNo, "main");
		}

		// 6. 향 계열 수정
		productAccordDao.deleteByProductNo(productNo);
		if (VO.getAccords() != null && !VO.getAccords().isEmpty()) {
			for (String accordName : VO.getAccords()) {
				Integer accordNo = accordDao.findAccordNoByName(accordName);
				if (accordNo != null) {
					productAccordDao.insert(new ProductAccordDto(productNo, accordNo));
				}
			}
		}
	}

	// ✅ 상품 삭제
	@Transactional
	public void deleteProductWithDependencies(long productNo) {
		volumeDao.deleteByProductNo(productNo);
		productImgDao.deleteByProductNo(productNo);
		perfumeDetailsDao.deleteByProductNo(productNo);
		reviewsDao.deleteByProductNo(productNo);
		cartDao.deleteByProductNo(productNo);
		productsDao.delete(productNo);
	}

	// ✅ 상품 상세 HTML 저장
	public void saveProductHtml(long productNo, String html) {
		productsDao.updateProductHtml(productNo, html);
	}

	// ✅ 상품 상세 HTML 조회
	public String getProductHtml(long productNo) {
		return productsDao.findProductHtml(productNo);
	}

	// ✅ 전체 목록
	public List<ProductListVO> getProductList(Integer accordNo, Integer categoryNo) {
		List<ProductListVO> list = productsDao.selectListVO(accordNo, categoryNo);
		for (ProductListVO product : list) {
			if (product.getAttachmentNo() != null) {
				product.setImageUrl("http://localhost:8080/api/attachment/" + product.getAttachmentNo());
			} else {
				product.setImageUrl("/no-image.png");
			}
		}
		return list;
	}

	// ✅ 카테고리별 목록
	public List<ProductListVO> getProductListByCategory(int categoryNo) {
		List<ProductListVO> list = productsDao.selectByCategory(categoryNo);
		for (ProductListVO product : list) {
			if (product.getAttachmentNo() != null) {
				product.setImageUrl("http://localhost:8080/api/attachment/" + product.getAttachmentNo());
			} else {
				product.setImageUrl("/no-image.png");
			}
		}
		return list;
	}
	
	//최신상품 조회
	public List<ProductListVO> getNewProducts(){
		List<ProductListVO> list = productsDao.selectNewProducts();
		for(ProductListVO productListVO : list) {
			if(productListVO.getAttachmentNo() != null) {
				productListVO.setImageUrl("http://localhost:8080/api/attachment/"+productListVO.getAttachmentNo());
			} else {
				productListVO.setImageUrl("/no-image.png");
			}
		}
		return list;
	}
	//할인상품 조회
	public List<ProductListVO> getSpecialPriceProduct(){
		List<ProductListVO> list = productsDao.selectSpecialPriceProducts();
		for(ProductListVO productListVO : list ) {
			if(productListVO.getAttachmentNo() != null) {
				productListVO.setImageUrl("http://localhost:8080/api/attachment/"+productListVO.getAttachmentNo());
			}else {
				productListVO.setImageUrl("/no-image.png");
			}
		}
		return list;
	}
	
}
