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

@CrossOrigin //프론트(React 등)에서 다른 포트에서 호출해도 허용
@RestController //Json 형식 응답을 위한 REST API 전용 컨트롤러
@RequestMapping("/api/products") //모든 API경로 앞에 /api/products 자동추가
public class ProductsRestController {

    @Autowired private ProductsDao productsDao;
    @Autowired private ProductService productService;
    @Autowired private ProductDetailDao productDetailDao;

    // ✅ 통합 등록 - 제일 먼저!
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  //프론트가 multipart/form-data 형식으로 요청을 보내야 함을 명시 
    public ResponseEntity<?> register( //<?>이건 어떤 타입이든 반환 가능하다는 의미
        @RequestPart("data") ProductAddRequestVO VO, //프론트에서 보낸 formdata중에 data 부분을 ProductAddRequestVO로 감싸겠다
        @RequestPart(value = "image", required = false) MultipartFile image //프론트에서 image라는 이름으로 넘어온 파일을 multipartfile 객체로 받음
    ) throws IOException { //예외는 던져버림 (이미지 파일을 getInputStream(),getBytes()로 읽을 때 i/o 가 발생하는데 혹시모를 읽기실패를 위해 작성한거임 
        productService.registerProduct(VO, image); //서비스로 보냄
        return ResponseEntity.ok("상품 등록 완료!"); //프론트에 상품등록완료 보내라 
    }

    // 전체 조회
    @GetMapping
    public List<ProductsDto> list(@RequestParam(required = false) Integer accordNo) { //안보내도 됨 , ?accordNo=1 처럼 url에 붙은 쿼리 파라미터 받을거
        if (accordNo != null) { // 향 계열 번호가 들어온 경우 
            return productService.getProductsByAccord(accordNo); //서비스 getProductsByAccord에 번호를 담아서 넘긴다
        } else {
            return productService.getAllProducts(); //그게 아니라면 해당 서비스에 getAllProducts 실행해라 
        }
    }
    // 상세 조회
    @GetMapping("/{productsNo}")
    public ProductsDto find(@PathVariable Integer productsNo) { //조회 경로의 productsNo를 받아서 productsNo라는 변수에 저장
        ProductsDto productsDto = productsDao.selectOne(productsNo); //dao의 selectone에 productsNo를 담아서 호출한다음 dto를 가져옴 
        if (productsDto == null) throw new TargetNotFoundException(); //결과가 없다면 커스텀 예외로 던져버림
        return productsDto;//있으면 dto 줘라 
    }

    // 삭제
    @DeleteMapping("/{productsNo}")
    public void delete(@PathVariable Integer productsNo) { //조회 경로의 productsNo 받아와라 
        ProductsDto productsDto = productsDao.selectOne(productsNo); //그 번호로 dao의 selectOne으로 조회해라 
        if (productsDto == null) throw new TargetNotFoundException(); //결과 없다면 예외로 던져라 
        productsDao.delete(productsNo); // 있다면 dao에 delete구문에 넣어라 (삭제)
    }

    // 수정
    @PatchMapping("/{productsNo}")
    public void edit(@PathVariable Integer productsNo, // 위 경로의 productsNo를 받아와라 
                     @RequestBody ProductsDto productsDto) { //프론트에서 보낸 JSON을 productsDto 객체로 바인딩해라 
        ProductsDto targetDto = productsDao.selectOne(productsNo); //productsNo 로 dao의 selectOne을 실행해서 targetDto에 넣어라(조회)
        if (targetDto == null) throw new TargetNotFoundException(); //없다면 예외로 던져라
        productsDto.setProductNo(productsNo); //상품번호를 dto에 강제로 세팅, 프론트에서 보낸 json에는 번호가 없을 수도 있음 하지만 update할때는 상품번호가 필수이므로 url에서 받은 번호 강제로 넣는 작업 없으면 업데이트실패 or 잘못된 데이터 수정 가능성있음 
        productsDao.update(productsDto); // 얻은 dto를 dao update 구문에 넣어라 
    }
    //상품 디테일 
    @GetMapping("/detail/{productNo}")
    public ProductDetailVO getProductDetail(@PathVariable int productNo) { //위 경로에 productNo를 받아와라 반환타입은 ProductDetailVO 다 
    	ProductDetailVO detail = productDetailDao.selectDetail(productNo); //해당 productNo로 dao에 selectDetail에 넣어라 
        detail.setVolumes(productDetailDao.selectVolumes(productNo));//해당번호에 해당하는 용량들을 detail에 추가해라
        return detail; //완성된 ProductDetailVO 객체를 넘겨줘라 
    }
    
}
