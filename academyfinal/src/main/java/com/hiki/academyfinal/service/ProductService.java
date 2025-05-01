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
import com.hiki.academyfinal.vo.VolumeInputVO;

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

    /**
     * 전체 상품 목록 조회 (이미지 URL 포함)
     */
    public List<ProductsDto> getAllProducts() {
        List<ProductsDto> list = productsDao.selectList();

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

    /**
     * 특정 향 계열(accordNo)에 속한 상품 목록 조회
     */
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

    /**
     * 통합 상품 등록 서비스 (상품 + 상세 + 용량 + 이미지 + 향계열 연결)
     * 트랜잭션 적용 (중간 실패 시 전체 롤백)
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerProduct(ProductAddRequestVO VO, MultipartFile image) throws IOException {
        // [1] 상품 기본 정보 등록
        int productNo = productsDao.sequence(); // 상품 번호 시퀀스 먼저 생성
        ProductsDto productsDto = ProductsDto.builder()
            .productNo(productNo)
            .productName(VO.getProductName())
            .productDetail(VO.getProductDetail())
            .productPrice(VO.getProductPrice())
            .brandNo(VO.getBrandNo())
            .strength(VO.getStrength())
            .categoryNo(VO.getCategoryNo())
            .productStock(0) // 기본 재고 0으로 설정
            .build();
        productsDao.insert(productsDto);

        // [2] 향수 상세 노트 등록
        int perfumeNo = perfumeDetailsDao.sequence();
        PerfumeDetailsDto perfumeDto = PerfumeDetailsDto.builder()
            .perfumeNo(perfumeNo)
            .productNo(productNo)
            .topNote(VO.getTopNote())
            .middleNote(VO.getMiddleNote())
            .baseNote(VO.getBaseNote())
            .build();
        perfumeDetailsDao.insert(perfumeDto);

        // [3] 용량 및 재고 등록
        for (VolumeInputVO input : VO.getVolumes()) {
            int volumeNo = volumeDao.sequence();
            VolumeDto volume = VolumeDto.builder()
                .volumeNo(volumeNo)
                .productNo(productNo)
                .volumeMl(input.getVolumeMl())
                .volumeStock(input.getVolumeStock())
                .build();
            volumeDao.insert(volume);
        }

        // [4] 이미지 등록 및 저장 (선택)
        if (image != null && !image.isEmpty()) {
            int attachNo = attachmentDao.sequence(); // 첨부파일 번호 시퀀스

            AttachmentDto attachDto = AttachmentDto.builder()
                .attachmentNo(attachNo)
                .attachmentName(image.getOriginalFilename())
                .attachmentType(image.getContentType())
                .attachmentSize(image.getSize())
                .build();

            try {
                attachmentDao.insert(attachDto); // DB에 파일 정보 저장
                attachmentService.save(image, attachNo); // 파일 시스템에 저장
                productImgDao.insert(productNo, attachNo); // 상품-파일 연결
            } catch (Exception e) {
                log.error("이미지 등록 실패, 롤백 수행", e);
                throw new RuntimeException("파일 저장 실패, 트랜잭션 롤백", e); // 반드시 예외 발생시켜야 롤백됨
            }
        }

        // [5] 향 계열(Accord) 매핑
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
}
