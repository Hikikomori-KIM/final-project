package com.hiki.academyfinal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.*;
import com.hiki.academyfinal.dto.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    @Autowired private ProductsDao productsDao;
    @Autowired private PerfumeDetailsDao perfumeDetailsDao;
    @Autowired private VolumeDao volumeDao;
    @Autowired private AttachmentDao attachmentDao;
    @Autowired private ProductImgDao productImgDao;
    @Autowired private AttachmentService attachmentService;
    @Autowired private AccordDao accordDao;
    @Autowired private ProductAccordDao productAccordDao;

    public List<ProductsDto> getProductsByAccord(int accordNo) {
        List<ProductsDto> list = productsDao.selectByAccord(accordNo);

        for (ProductsDto product : list) {
            Integer attachmentNo = productImgDao.findAttachmentNoByProductNo(product.getProductNo());
            if (attachmentNo != null) {
                product.setImageUrl("http://localhost:8080/api/attachment/" + attachmentNo);
            } else {
                product.setImageUrl("/no-image.png");
            }
        }

        return list;
    }
    
    public List<ProductsDto> getAllProducts() {
        List<ProductsDto> list = productsDao.selectList(); // 전체 상품 조회

        for (ProductsDto product : list) {
            Integer attachmentNo = productImgDao.findAttachmentNoByProductNo(product.getProductNo());
            if (attachmentNo != null) {
                product.setImageUrl("http://localhost:8080/api/attachment/" + attachmentNo);
            } else {
                product.setImageUrl("/no-image.png");
            }
        }

        return list;
    }
    
    // 통합 상품 등록
    public void registerProduct(ProductAddRequestDto dto, MultipartFile image) throws IOException {
        // 1. 상품 등록
        int productNo = productsDao.sequence(); // 시퀀스 먼저 생성
        ProductsDto productsDto = ProductsDto.builder()
            .productNo(productNo)
            .productName(dto.getProductName())
            .productDetail(dto.getProductDetail())
            .productPrice(dto.getProductPrice())
            .brandNo(dto.getBrandNo()) // 브랜드 번호
            .strength(dto.getStrength())
            .categoryNo(dto.getCategoryNo())
            .productStock(0) // ✅ 기본 재고 0으로 세팅
            .build();
        productsDao.insert(productsDto);

        // 2. 향수 상세 정보 등록
        int perfumeNo = perfumeDetailsDao.sequence();
        PerfumeDetailsDto perfumeDto = PerfumeDetailsDto.builder()
        	    .perfumeNo(perfumeNo)         // ✅ 시퀀스 넣기
        	    .productNo(productNo)         // ✅ 연결할 상품 번호 넣기
        	    .topNote(dto.getTopNote())
        	    .middleNote(dto.getMiddleNote())
        	    .baseNote(dto.getBaseNote())
        	    .build();

        	perfumeDetailsDao.insert(perfumeDto);

        // 3. 용량 정보 등록
        for (VolumeDto volume : dto.getVolumes()) {
            int volumeNo = volumeDao.sequence();
            volume.setVolumeNo(volumeNo);
            volume.setProductNo(productNo);
            volumeDao.insert(volume);
        }

        // 4. 이미지 업로드 처리 (선택)
        if (image != null && !image.isEmpty()) {
            int attachNo = attachmentDao.sequence();

            AttachmentDto attachDto = AttachmentDto.builder()
                .attachmentNo(attachNo)
                .attachmentName(image.getOriginalFilename())
                .attachmentType(image.getContentType())
                .attachmentSize(image.getSize())
                .build();

            try {
                attachmentDao.insert(attachDto);
                attachmentService.save(image, attachNo);
                productImgDao.insert(productNo, attachNo); // ← 이건 insert 성공 이후에만
            } catch (Exception e) {
                log.error("이미지 등록 실패", e);
                // 필요시 사용자 알림도 추가 가능
            }
        }

        // 5. 향 계열(Accords) 연결 등록
        if (dto.getAccords() != null && !dto.getAccords().isEmpty()) {
            for (String accordName : dto.getAccords()) {
                Integer accordNo = accordDao.findAccordNoByName(accordName);
                if (accordNo != null) {
                    ProductAccordDto productAccordDto = new ProductAccordDto(productNo, accordNo);
                    productAccordDao.insert(productAccordDto);
                }
            }
        }
    }
}
