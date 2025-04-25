package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.FaqDao;
import com.hiki.academyfinal.dto.FaqDto;
import com.hiki.academyfinal.error.TargetNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/service/faq")
public class FaqRestController {
	
	@Autowired
	private FaqDao faqDao;
	
	@GetMapping("/")
	public List<FaqDto> list() {
		return faqDao.selectList();
	}
	
	@GetMapping("/{faqNo}")
	public FaqDto find(@PathVariable long faqNo) {
		FaqDto faqDto = faqDao.selectOne(faqNo);
		if(faqDto == null) throw new TargetNotFoundException();
		return faqDto;
	}

}
