package com.hiki.academyfinal.service;

import java.io.IOException;
import java.util.List;

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

	/**
	 * 전체 상품 목록 조회 (ProductListVO 기준, accordNo/categoryNo 필터 적용 가능)
	 */
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

	/**
	 * 상품 통합 등록 (상품 + 향수 상세 + 용량 + 이미지 + 향계열 연결)
	 */
	@Transactional(rollbackFor = Exception.class)
	public void registerProduct(ProductAddRequestVO VO, MultipartFile image) throws IOException {
		// [1] 상품 정보 등록
		int productNo = productsDao.sequence();
		ProductsDto productsDto = ProductsDto.builder().productNo(productNo).productName(VO.getProductName())
				.productDetail(VO.getProductDetail()).productPrice(VO.getProductPrice()).discountRate(VO.getDiscountRate())
				.discountedPrice(VO.getDiscountedPrice()).brandNo(VO.getBrandNo())
				.strength(VO.getStrength()).categoryNo(VO.getCategoryNo()).productStock(0)
				.productDescriptionHtml(VO.getProductDescriptionHtml()).build();
		productsDao.insert(productsDto);

		// [2] 향수 상세 노트 등록
		int perfumeNo = perfumeDetailsDao.sequence();
		PerfumeDetailsDto perfumeDto = PerfumeDetailsDto.builder().perfumeNo(perfumeNo).productNo(productNo)
				.topNote(VO.getTopNote()).middleNote(VO.getMiddleNote()).baseNote(VO.getBaseNote()).build();
		perfumeDetailsDao.insert(perfumeDto);

		// [3] 용량 등록
		for (VolumeInputVO input : VO.getVolumes()) {
			int volumeNo = volumeDao.sequence();
			VolumeDto volume = VolumeDto.builder().volumeNo(volumeNo).productNo(productNo).volumeMl(input.getVolumeMl())
					.volumeStock(input.getVolumeStock()).volumePrice(input.getVolumePrice()).build();
			volumeDao.insert(volume);
		}

		// [4] 이미지 등록
		if (image != null && !image.isEmpty()) {
			int attachNo = attachmentDao.sequence();
			AttachmentDto attachDto = AttachmentDto.builder().attachmentNo(attachNo)
					.attachmentName(image.getOriginalFilename()).attachmentType(image.getContentType())
					.attachmentSize(image.getSize()).build();
			try {
				attachmentDao.insert(attachDto);
				attachmentService.save(image, attachNo);
				productImgDao.insert(productNo, attachNo, "main");
			} catch (Exception e) {
				log.error("이미지 등록 실패", e);
				throw new RuntimeException("파일 저장 실패", e);
			}
		}

		// [5] 향 계열 매핑
		if (VO.getAccords() != null && !VO.getAccords().isEmpty()) {
			for (String accordName : VO.getAccords()) {
				Integer accordNo = accordDao.findAccordNoByName(accordName);
				if (accordNo != null) {
					ProductAccordDto mapping = new ProductAccordDto(productNo, accordNo);
					productAccordDao.insert(mapping);
				}
			}
		}
	}

	/**
	 * 상품 및 관련 데이터 삭제
	 */
	@Transactional
	public void deleteProductWithDependencies(int productNo) {
		volumeDao.deleteByProductNo(productNo);
		productImgDao.deleteByProductNo(productNo);
		perfumeDetailsDao.deleteByProductNo(productNo);
		reviewsDao.deleteByProductNo(productNo);
		cartDao.deleteByProductNo(productNo);
		productsDao.delete(productNo);
	}

	@Transactional
	public void updateVolumes(int productNo, List<VolumeInputVO> volumes) {
		for (VolumeInputVO input : volumes) {
			VolumeDto dto = VolumeDto.builder().productNo(productNo).volumeMl(input.getVolumeMl())
					.volumeStock(input.getVolumeStock()).volumePrice(input.getVolumePrice()).build();
			volumeDao.updateVolume(dto);
		}
	}

	/**
	 * HTML 저장
	 */
	public void saveProductHtml(int productNo, String html) {
		productsDao.updateProductHtml(productNo, html);
	}

	/**
	 * HTML 조회
	 */
	public String getProductHtml(int productNo) {
		return productsDao.findProductHtml(productNo);
	}
	//카테고리 이미지
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
}
