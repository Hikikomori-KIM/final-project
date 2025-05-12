package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.ProductDetailViewDao;
import com.hiki.academyfinal.dao.ScentListViewDao;
import com.hiki.academyfinal.dao.ScentRecommendationDao;
import com.hiki.academyfinal.dto.ProductDetailViewDto;
import com.hiki.academyfinal.dto.ScentListViewDto;
import com.hiki.academyfinal.dto.ScentRecommendationDto;
import com.hiki.academyfinal.service.SurveyService;
import com.hiki.academyfinal.vo.RequestMakeSurveyVO;


@CrossOrigin
@RestController
@RequestMapping("/api/scent")
public class ScentRestController {

	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private ScentListViewDao scentListViewDao;
	
	@Autowired
	private ScentRecommendationDao scentRecommendationDao;
	
	@Autowired
	private ProductDetailViewDao productDetailViewDao;
	
//	@PostMapping("/make")
//	public  ResponseEntity<String> scentSuvAdd(@RequestBody RequestMakeSurveyVO requestMakeSurveyVO) {
//		//1.메인질문 시퀀스저장+메인질문저장 //2.for문돌려서 하위선택지들에 질문지no저장 + 저장 //3.이걸반복
//		//1-1카운트 추가 시퀀스미리뽑기가능 service가즈아..dao에서하기도힘들듯 ,,
//		System.out.println(requestMakeSurveyVO);
//		makeSurveyService.MakeSurvey(requestMakeSurveyVO);
//		return ResponseEntity.ok("즐");
//	}

// //db 최대 31번접근  (실력맞춤 하위호환 최대질문지10개제한 선택5제한임= 총1개의 테스트)
//	질문지 시퀀스 조회1번/ 질문지 insert 10번 /선택지 시퀀스 조회10번/선택지 insert 10번
	//목표는 최대한구현 + db최소접근
	@PostMapping("/make")
	public ResponseEntity<String> scentSuvAdd(@RequestBody List<RequestMakeSurveyVO> requestList) {
	   surveyService.MakeSurvey(requestList);
	    return ResponseEntity.ok("성공");
	}

	//관리자의 작성목록 
	@GetMapping("/load")
	public List<ScentListViewDto> listView(){
		return scentListViewDao.scentList();
	}
	
	//업데이트 view로받아서 쪼개서처리
	@PutMapping("/update")
	public ResponseEntity<String> updateSurvey(@RequestBody List<ScentListViewDto> scentListViewDto){
		return surveyService.updateAll(scentListViewDto);
	}
	
	@DeleteMapping("/delete/{scentQuestionNo}")
	public void deleteQuestion(@PathVariable int scentQuestionNo) {
		surveyService.deleteQuestion(scentQuestionNo);
	}
	//관리자 매칭리스트
	@GetMapping("/list")
	public List<ScentRecommendationDto> list(){
		return scentRecommendationDao.list();
	} 
	//매칭 프로덕트리스트 보여주기(탑앤쿼리와 검색은 시간나면추가)이게ㅐ중요한게아님 유저가 선택하는거까지 가는게중요
	@GetMapping("/productList")
	public List<ProductDetailViewDto> productList(){
		return productDetailViewDao.productList();
	}
	
	//매칭 프로덕트 변경 아 실수했네 여기서하면안되는데..
	@PatchMapping("/changeProduct/{productNo}")
	public ResponseEntity<String> changeProduct(@PathVariable long productNo,@RequestBody ScentRecommendationDto scentRecommendationDto){
		scentRecommendationDao.changeProduct(productNo, scentRecommendationDto.getScentRecommendationNo());
		return ResponseEntity.ok("변경완료");
	}
	
	@PatchMapping("/change/{scentRecommendationNo}")
		public ResponseEntity<String> changeComment(@PathVariable int scentRecommendationNo, @RequestBody ScentRecommendationDto scentRecommendationDto){
		scentRecommendationDao.changeComment(scentRecommendationNo, scentRecommendationDto.getScentRecommendationComment());
		return ResponseEntity.ok("변경완료");	
	}
	
	/////////////////////위에 관리자 밑엔 유저///////////////////////////
	
	@GetMapping("/surveyList")
	public List<ScentListViewDto> surveyList(){
		return scentListViewDao.scentList();
	}
}
