package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.BrandDao;
import com.hiki.academyfinal.dto.BrandDto;

@CrossOrigin
@RestController
@RequestMapping("/api/brands")
public class BrandRestController {
	@Autowired
	public BrandDao brandDao;
	
	@GetMapping
	public List<BrandDto> list(){
		return brandDao.selectList();
	}
	
}
