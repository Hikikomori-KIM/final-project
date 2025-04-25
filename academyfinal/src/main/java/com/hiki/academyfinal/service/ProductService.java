package com.hiki.academyfinal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.AttachmentDao;
import com.hiki.academyfinal.dao.PerfumeDetailsDao;
import com.hiki.academyfinal.dao.ProductImgDao;
import com.hiki.academyfinal.dao.ProductsDao;
import com.hiki.academyfinal.dao.VolumeDao;
import com.hiki.academyfinal.dto.AttachmentDto;
import com.hiki.academyfinal.dto.PerfumeDetailsDto;
import com.hiki.academyfinal.dto.ProductAddRequestDto;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.dto.VolumeDto;
import com.hiki.academyfinal.service.AttachmentService;

@Service
public class ProductService {

    @Autowired private ProductsDao productsDao;
    @Autowired private PerfumeDetailsDao perfumeDetailsDao;
    @Autowired private VolumeDao volumeDao;
    @Autowired private AttachmentDao attachmentDao;
    @Autowired private ProductImgDao productImgDao;
    @Autowired private AttachmentService attachmentService;

    // 통합 상품 등록
    public void registerProduct(ProductAddRequestDto dto, MultipartFile image) throws IOException {
        // 1. 상품 등록
        ProductsDto productsDto = ProductsDto.builder()
            .productName(dto.getProductName())
            .productDetail(dto.getProductDetail())
            .productPrice(dto.getProductPrice())
            .brand(dto.getBrand())
            .strength(dto.getStrength())
            .build();

        int productNo = productsDao.sequence();
        productsDto.setProductNo(productNo);
        productsDao.insert(productsDto);

        // 2. 향수 정보 등록
        PerfumeDetailsDto perfumeDto = PerfumeDetailsDto.builder()
            .productNo(productNo)
            .topNote(dto.getTopNote())
            .middleNote(dto.getMiddleNote())
            .baseNote(dto.getBaseNote())
            .build();
        perfumeDetailsDao.insert(perfumeDto);

        // 3. 용량들 저장
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

            attachmentDao.insert(attachDto);
            attachmentService.save(image, attachNo); // 실제 파일 저장
            productImgDao.insert(productNo, attachNo); // 이미지-상품 연결
        }
    }
}
