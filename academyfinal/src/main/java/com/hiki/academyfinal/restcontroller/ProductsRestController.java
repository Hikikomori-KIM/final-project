package com.hiki.academyfinal.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hiki.academyfinal.dao.ProductDetailDao;
import com.hiki.academyfinal.dao.ProductsDao;
import com.hiki.academyfinal.dto.ProductsDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.service.ProductService;
import com.hiki.academyfinal.vo.ProductAddRequestVO;
import com.hiki.academyfinal.vo.ProductDetailVO;
import com.hiki.academyfinal.vo.ProductListVO;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductsRestController {

	@Autowired
	private ProductsDao productsDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductDetailDao productDetailDao;

	// ✅ 통합 등록
	@PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> register(@RequestPart("data") ProductAddRequestVO VO,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		productService.registerProduct(VO, image);
		return ResponseEntity.ok("상품 등록 완료!");
	}

	// ✅ 간략 리스트 (ProductListVO 기반)
	@GetMapping
	public List<ProductListVO> list(@RequestParam(required = false) Integer accordNo,
			@RequestParam(required = false) Integer categoryNo) {
		return productService.getProductList(accordNo, categoryNo);
	}

	// 상품 단일 조회
	@GetMapping("/{productsNo}")
	public ProductsDto find(@PathVariable Integer productsNo) {
		ProductsDto productsDto = productsDao.selectOne(productsNo);
		if (productsDto == null)
			throw new TargetNotFoundException();
		return productsDto;
	}

	// 상품 수정
	@PatchMapping("/edit/{productsNo}")
	public void edit(@PathVariable Integer productsNo, @RequestBody ProductsDto productsDto) {
		ProductsDto targetDto = productsDao.selectOne(productsNo);
		if (targetDto == null)
			throw new TargetNotFoundException();
		productsDto.setProductNo(productsNo);
		productsDao.update(productsDto);
	}

	// 상품 상세 정보 + 용량 + 향계열
	@GetMapping("/detail/{productNo}")
	public ProductDetailVO getProductDetail(@PathVariable int productNo) {
		List<ProductDetailVO> list = productDetailDao.selectDetail(productNo);
		if (list.isEmpty())
			throw new TargetNotFoundException();

		ProductDetailVO detail = list.get(0); // ✅ 대표 데이터 한 개만 사용

		// 🔹 향 계열 직접 조회 후 세팅
		detail.setAccords(productDetailDao.selectAccords(productNo));

		// 🔹 용량 목록 조회 후 세팅
		detail.setVolumes(productDetailDao.selectVolumes(productNo));

		return detail;
	}

	// 상품 삭제
	@DeleteMapping("/{productNo}")
	public ResponseEntity<Void> delete(@PathVariable int productNo) {
		productService.deleteProductWithDependencies(productNo);
		return ResponseEntity.noContent().build();
	}

	// 상품 상세 HTML 조회
	@GetMapping("/html/{productNo}")
	public ResponseEntity<String> getProductHtml(@PathVariable int productNo) {
		String html = productService.getProductHtml(productNo);
		return ResponseEntity.ok(html);
	}

	// 상품 상세 HTML 수정
	@PatchMapping("/html/{productNo}")
	public ResponseEntity<?> updateProductHtml(@PathVariable int productNo, @RequestBody String html) {
		productService.saveProductHtml(productNo, html);
		return ResponseEntity.ok("상품 상세 HTML 저장 완료");
	}

	@GetMapping("/category/{categoryNo}")
	public List<ProductListVO> getByCategory(@PathVariable int categoryNo) {
		return productsDao.selectByCategory(categoryNo);
	}
}
