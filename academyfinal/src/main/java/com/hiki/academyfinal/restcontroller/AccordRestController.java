package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.AccordDao;
import com.hiki.academyfinal.dto.AccordDto;

@CrossOrigin
@RestController
@RequestMapping("/api/accords")
public class AccordRestController {
	@Autowired
	private AccordDao accordDao;
	
	@GetMapping
	public List<AccordDto> list(){
		return accordDao.selectList();
	}
}
