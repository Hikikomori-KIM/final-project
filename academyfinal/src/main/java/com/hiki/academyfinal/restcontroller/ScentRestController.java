package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.service.MakeSurveyService;
import com.hiki.academyfinal.vo.RequestMakeSurveyVO;


@CrossOrigin
@RestController
@RequestMapping("/api/scent")
public class ScentRestController {

	@Autowired
	private MakeSurveyService makeSurveyService;
	
//	@PostMapping("/make")
//	public  ResponseEntity<String> scentSuvAdd(@RequestBody RequestMakeSurveyVO requestMakeSurveyVO) {
//		//1.메인질문 시퀀스저장+메인질문저장 //2.for문돌려서 하위선택지들에 질문지no저장 + 저장 //3.이걸반복
//		//1-1카운트 추가 시퀀스미리뽑기가능 service가즈아..dao에서하기도힘들듯 for문존내돌려,,
//		System.out.println(requestMakeSurveyVO);
//		makeSurveyService.MakeSurvey(requestMakeSurveyVO);
//		return ResponseEntity.ok("즐");
//	}

// //db 최대 31번접근  (실력맞춤 하위호환 최대질문지10개제한 선택5제한임)
//	질문지 시퀀스 조회1번/ 질문지 insert 10번 /선택지 시퀀스 조회10번/선택지 insert 10번
	//목표는 최대한구현 + db최소접근
	@PostMapping("/make")
	public ResponseEntity<String> scentSuvAdd(@RequestBody List<RequestMakeSurveyVO> requestList) {
	    makeSurveyService.MakeSurvey(requestList);
	    return ResponseEntity.ok("성공");
	}

}
