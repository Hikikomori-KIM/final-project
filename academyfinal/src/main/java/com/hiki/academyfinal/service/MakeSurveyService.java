package com.hiki.academyfinal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiki.academyfinal.dao.ScentChoiceDao;
import com.hiki.academyfinal.dao.ScentQuestionDao;
import com.hiki.academyfinal.dto.ScentChoiceDto;
import com.hiki.academyfinal.dto.ScentQuestionDto;
import com.hiki.academyfinal.vo.RequestMakeSurveyVO;

@Service
public class MakeSurveyService {

	@Autowired
	private ScentQuestionDao scentQuestionDao;
	
	@Autowired
	private ScentChoiceDao scentChoiceDao;
	@Transactional
	public void MakeSurvey(List<RequestMakeSurveyVO> requestMakeSurveyVO) {
	    // 1. 카운트를 먼저 봐볼까유..퀘스천시퀀스를 한번봅시다
	    List<Integer> sequenceList = scentQuestionDao.sequence(requestMakeSurveyVO.get(0).getCount());

	    // 시퀀스 dao뽑은거 저장 외부 개수만큼(빡셀줄알고 리액트서 카운트들고왔디비
	    //외부!
	    for (int i = 0; i < requestMakeSurveyVO.get(0).getCount(); i++) {
	        int mainSequence = sequenceList.get(i);

	        // 질문지선택임 안쪽질문지
	        List<ScentChoiceDto> scentChoices = requestMakeSurveyVO.get(i).getScentChoiceDto();
	        //하나씩 질문지no저장을한다
	        //내부 ! 내부는 선택지저장!
	        if (scentChoices != null) {
	        for (ScentChoiceDto scentChoice : scentChoices) {
	            scentChoice.setScentQuestionNo(mainSequence); // 선택지에 질문 번호 매핑
	        }
	        scentChoiceDao.insertAll(scentChoices);
	        }
	        // 외부!외부는 질문지저장!
	        ScentQuestionDto scentQuestionDto = ScentQuestionDto.builder()
								        .scentQuestionNo(mainSequence)
								        .scentQuestionContent(requestMakeSurveyVO.get(i).getScentQuestionContent())
								        .build();
	        scentQuestionDao.insert(scentQuestionDto);
	    }
	}

//    @Transactional
//    public void MakeSurvey(RequestMakeSurveyVO requestMakeSurveyVO) {
//        // 1. 카운트를 먼저 보고 퀘스천 시퀀스를 뽑기
//    	System.out.println(requestMakeSurveyVO);
//        List<Integer> sequenceList = scentQuestionDao.sequence(requestMakeSurveyVO.getCount());
//        // 2. 외부 질문과 선택지를 처리
//        for (int i = 0; i < requestMakeSurveyVO.getCount(); i++) {
//            int mainSequence = sequenceList.get(i);
//
//            // 3. 질문지 저장
//            ScentQuestionDto scentQuestionDto = ScentQuestionDto.builder()
//                .scentQuestionNo(mainSequence)
//                .scentQuestionContent(requestMakeSurveyVO.getScentQuestionContent())
//                .build();
//            scentQuestionDao.insert(scentQuestionDto);
//
//            // 4. 선택지 저장
//            List<ScentChoiceDto> scentChoices = requestMakeSurveyVO.getScentChoiceDto();
//            if (scentChoices != null && !scentChoices.isEmpty()) {
//                // 선택지에 질문 번호 매핑
//                for (int j = 0; j < scentChoices.size(); j++) {
//                    ScentChoiceDto scentChoice = scentChoices.get(j);
//                    scentChoice.setScentQuestionNo(mainSequence);
//                }
//                scentChoiceDao.insertAll(scentChoices);
//            }
//        }
//    }


}
