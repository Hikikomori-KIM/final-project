package com.hiki.academyfinal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.hiki.academyfinal.dao.ScentChoiceDao;
import com.hiki.academyfinal.dao.ScentQuestionDao;
import com.hiki.academyfinal.dto.ScentChoiceDto;
import com.hiki.academyfinal.dto.ScentListViewDto;
import com.hiki.academyfinal.dto.ScentQuestionDto;
import com.hiki.academyfinal.vo.RequestMakeSurveyVO;

@Service
public class SurveyService {

	@Autowired
	private ScentQuestionDao scentQuestionDao;
	
	@Autowired
	private ScentChoiceDao scentChoiceDao;
	@Transactional
	public void MakeSurvey(List<RequestMakeSurveyVO> requestMakeSurveyVO) {
		
	    // 1. 카운트를 먼저 봐볼까유..퀘스천시퀀스를 한번봅시다
	    List<Integer> sequenceList = scentQuestionDao.sequenceList(requestMakeSurveyVO.size());
	    // 시퀀스 dao뽑은거 저장 외부 개수만큼 하 어레이시퀀스안돼서 카운트뺌 나중에삭제해
	    //외부!
	    for (int i = 0; i < requestMakeSurveyVO.size(); i++) {
	        int mainSequence = sequenceList.get(i);
	        System.out.println(">> 리스트 크기: " + requestMakeSurveyVO.size());
	        System.out.println(">> get(0).getCount(): " + requestMakeSurveyVO.get(0).getCount());
	        System.out.println(">> 시퀀스 개수: " + sequenceList.size());
	        System.out.println("메인시퀀스"+mainSequence);
	        // 외부!외부는 질문지저장! 먼저해야 안터진댄다 이런
	        ScentQuestionDto scentQuestionDto = ScentQuestionDto.builder()
	        		.scentQuestionNo(mainSequence)
	        		.scentQuestionContent(requestMakeSurveyVO.get(i).getScentQuestionContent())
	        		.build();
	        scentQuestionDao.insert(scentQuestionDto);
	        // 질문지선택임 안쪽질문지
	        List<ScentChoiceDto> scentChoices = requestMakeSurveyVO.get(i).getScentChoiceDto();
	        //하나씩 질문지no저장을한다
	        //내부 ! 내부는 선택지저장!
	        if (scentChoices != null) {
	        	List<Integer>sequence = scentChoiceDao.sequenceList(scentChoices.size());
	        for (int k=0; k<scentChoices.size(); k++) {
	        	System.out.println(sequence.get(k));
	        	scentChoices.get(k).setScentChoiceNo(sequence.get(k));
	        	scentChoices.get(k).setScentQuestionNo(mainSequence); // 선택지에 질문 번호 매핑
	        }
	        scentChoiceDao.insertAll(scentChoices);
	        }
	    }
	}

	//업데이트 질문지1개통으로치기 (난허접이라 선택지는 폴문돌림)
	@Transactional
	public ResponseEntity<String> updateAll(@RequestBody List<ScentListViewDto> scentListViewDto) {  
	    ScentQuestionDto scentQuestionDto = ScentQuestionDto.builder()
	        .scentQuestionNo(scentListViewDto.get(0).getScentQuestionNo())
	        .scentQuestionContent(scentListViewDto.get(0).getScentQuestionContent()) 
	        .build();
	    scentQuestionDao.updateQuestion(scentQuestionDto);
	    for (ScentListViewDto dto : scentListViewDto) {
	        ScentChoiceDto scentChoiceDto = ScentChoiceDto.builder()
	            .scentChoiceNo(dto.getScentChoiceNo())
	            .scentQuestionNo(dto.getChoiceQuestionNo())
	            .scentChoiceContent(dto.getScentChoiceContent())
	            .scentChoiceType(dto.getScentChoiceType())
	            .scentChoiceScore(dto.getScentChoiceScore())
	            .build();
	        scentChoiceDao.updateChoice(scentChoiceDto);
	    }

	    return ResponseEntity.ok("업데이트 완료");
	}

	//삭제 질문지1개통(허접이라 선택지 폴문돌림ㅋ)
	@Transactional
	public void deleteQuestion(int scentQuestionNo) {
		//선택지먼저삭제
		List<Integer>noList = scentChoiceDao.questionNoList(scentQuestionNo);
		for(int no : noList){
			scentChoiceDao.deleteChoice(no);
		}
		scentQuestionDao.deleteQuestion(scentQuestionNo);
	}
}
