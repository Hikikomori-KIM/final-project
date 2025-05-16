package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.BrandDao;
import com.hiki.academyfinal.dto.BrandDto;

import org.springframework.web.bind.annotation.RequestBody; // ✅ 이걸로 바꿔야 함


@CrossOrigin
@RestController
@RequestMapping("/api/brands")
public class BrandRestController {
	@Autowired
	public BrandDao brandDao;
	//브랜드 리스트
	@GetMapping
	public List<BrandDto> list(){
		return brandDao.selectList();
	}
	//브랜드 등록
	@PostMapping
	public void insertBrand(@RequestBody BrandDto brandDto) {
		brandDao.insert(brandDto);
	}
	//브랜드 수정
	@PutMapping("/{brandNo}")
	public void updateBrand(@PathVariable long brandNo, @RequestBody BrandDto brandDto) {
		brandDto.setBrandNo(brandNo);
		brandDao.update(brandDto);
	}
	//브랜드 삭제
	@DeleteMapping("/{brandNo}")
	public void deleteBrand(@PathVariable int brandNo) {
		brandDao.delete(brandNo);
	}
	
	
}
