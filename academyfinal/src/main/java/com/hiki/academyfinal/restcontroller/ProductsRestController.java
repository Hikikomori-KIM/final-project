package com.hiki.academyfinal.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.ProductsDao;
import com.hiki.academyfinal.dto.ProductAddRequestDto;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductsRestController {

    @Autowired private ProductsDao productsDao;
    @Autowired private ProductService productService;

    // 전체 조회
    @GetMapping("/")
    public List<ProductsDto> list() {
        return productsDao.selectList();
    }

    // 단일 조회
    @GetMapping("/{productsNo}")
    public ProductsDto find(@PathVariable Integer productsNo) {
        ProductsDto productsDto = productsDao.selectOne(productsNo);
        if (productsDto == null) throw new TargetNotFoundException();
        return productsDto;
    }

    // 삭제
    @DeleteMapping("/{productsNo}")
    public void delete(@PathVariable Integer productsNo) {
        ProductsDto productsDto = productsDao.selectOne(productsNo);
        if (productsDto == null) throw new TargetNotFoundException();
        productsDao.delete(productsNo);
    }

    // 수정
    @PatchMapping("/{productsNo}")
    public void edit(@PathVariable Integer productsNo,
                     @RequestBody ProductsDto productsDto) {
        ProductsDto targetDto = productsDao.selectOne(productsNo);
        if (targetDto == null) throw new TargetNotFoundException();

        productsDto.setProductNo(productsNo);
        productsDao.update(productsDto);
    }

    // 통합 등록
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
        @RequestPart("data") ProductAddRequestDto dto,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        productService.registerProduct(dto, image);
        return ResponseEntity.ok("상품 등록 완료!");
    }
}
