package com.hiki.academyfinal.restcontroller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiki.academyfinal.dao.ProductDetailDao;
import com.hiki.academyfinal.dao.ProductsDao;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.ProductService;
import com.hiki.academyfinal.vo.ProductAddRequestVO;
import com.hiki.academyfinal.vo.ProductDetailVO;
import com.hiki.academyfinal.vo.ProductListVO;
import com.hiki.academyfinal.vo.ProductSalesVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductsRestController {

    @Autowired private ProductsDao productsDao;
    @Autowired private ProductService productService;
    @Autowired private ProductDetailDao productDetailDao;

    private final ObjectMapper mapper = new ObjectMapper();

    // ✅ 상품 등록 (이미지 포함)
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam("data") String json, // JSON 문자열
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "types", required = false) String typesJson // JSON 배열 문자열
    ) throws IOException {
        ProductAddRequestVO VO = mapper.readValue(json, ProductAddRequestVO.class);
        List<String> types = mapper.readValue(typesJson, new TypeReference<List<String>>() {});
        productService.registerProduct(VO, images, types);
        return ResponseEntity.ok("상품 등록 완료!");
    }

    // ✅ 상품 수정 (이미지 포함)
    @PatchMapping(value = "/edit/{productNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editProduct(
            @PathVariable long productNo,
            @RequestPart("data") String json,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            ProductAddRequestVO vo = mapper.readValue(json, ProductAddRequestVO.class);
            productService.updateProduct(productNo, vo, image);
            return ResponseEntity.ok("상품 수정 완료!");
        } catch (Exception e) {
            e.printStackTrace(); // 🔍 디버깅용
            return ResponseEntity.internalServerError().body("상품 수정 실패: " + e.getMessage());
        }
    }

    // ✅ 전체 상품 리스트
    @GetMapping
    public List<ProductListVO> list(@RequestParam(required = false) Integer accordNo,
                                    @RequestParam(required = false) Integer categoryNo) {
        return productService.getProductList(accordNo, categoryNo);
    }

    // ✅ 상품 단일 조회
    @GetMapping("/{productsNo}")
    public ProductsDto find(@PathVariable Integer productsNo) {
        ProductsDto productsDto = productsDao.selectOne(productsNo);
        if (productsDto == null) throw new TargetNotFoundException();
        return productsDto;
    }

    // ✅ 상품 상세 정보 (향 계열, 용량 포함)
    @GetMapping("/detail/{productNo}")
    public ProductDetailVO getProductDetail(@PathVariable long productNo) {
        List<ProductDetailVO> list = productDetailDao.selectDetail(productNo);
        if (list.isEmpty()) throw new TargetNotFoundException();

        ProductDetailVO detail = list.get(0); // 대표 정보
        detail.setAccords(productDetailDao.selectAccords(productNo));
        detail.setVolumes(productDetailDao.selectVolumes(productNo));
        return detail;
    }

    // ✅ 상품 삭제
    @DeleteMapping("/{productNo}")
    public ResponseEntity<Void> delete(@PathVariable long productNo) {
        productService.deleteProductWithDependencies(productNo);
        return ResponseEntity.noContent().build();
    }

    // ✅ HTML 설명 조회
    @GetMapping("/html/{productNo}")
    public ResponseEntity<String> getProductHtml(@PathVariable long productNo) {
        String html = productService.getProductHtml(productNo);
        return ResponseEntity.ok(html);
    }

    // ✅ HTML 설명 수정
    @PatchMapping("/html/{productNo}")
    public ResponseEntity<?> updateProductHtml(@PathVariable long productNo, @RequestBody String html) {
        productService.saveProductHtml(productNo, html);
        return ResponseEntity.ok("상품 상세 HTML 저장 완료");
    }

    // ✅ 카테고리별 목록
    @GetMapping("/category/{categoryNo}")
    public List<ProductListVO> getByCategory(@PathVariable int categoryNo) {
        return productService.getProductListByCategory(categoryNo);
    }
    //판매량 기준 전체상품 조회 
    @GetMapping("/best")
    public List<ProductSalesVO> getBestProducts(){
    	return productsDao.selectBestProducts();
    }
    //MD픽 
    @PatchMapping("/mdpick/{productNo}") // ✅ 기존: /products/{productNo}/md-pick
    public ResponseEntity<?> updateMdPick(
            @PathVariable long productNo,
            @RequestBody Map<String, String> body) {
        String mdPick = body.get("mdPick"); // 'Y' 또는 'N'
        productsDao.updateMdPick(productNo, mdPick);
        return ResponseEntity.ok().build();
    }
}
