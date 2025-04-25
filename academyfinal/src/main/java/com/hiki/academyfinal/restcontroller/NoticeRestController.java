package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.NoticeDao;
import com.hiki.academyfinal.dto.NoticeDto;
import com.hiki.academyfinal.error.TargetNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/service/notice")
public class NoticeRestController {
	
	@Autowired
	private NoticeDao noticeDao;
	
	@GetMapping("/")
	public List<NoticeDto> list() {
		return noticeDao.selectList();
	}
	
	@GetMapping("/{noticeNo}")
	public NoticeDto detail(@PathVariable long noticeNo) {	
		NoticeDto noticeDto = noticeDao.selectOne(noticeNo);
		if(noticeDto == null) throw new TargetNotFoundException();
		return noticeDto;
	}	
}
