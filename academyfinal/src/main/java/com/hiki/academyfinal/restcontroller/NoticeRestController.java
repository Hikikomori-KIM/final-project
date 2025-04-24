package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.NoticeDao;
import com.hiki.academyfinal.dto.NoticeDto;

@CrossOrigin
@RestController
@RequestMapping("/api/notice")
public class NoticeRestController {
	
	@Autowired
	private NoticeDao noticeDao;
	
	@GetMapping("/list")
	public List<NoticeDto> list() {
		return noticeDao.selectList();
	}
	
}
