package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	//어코드 등록
	@PostMapping
	public void insertAccord(@RequestBody AccordDto accordDto) {
		accordDao.insert(accordDto);
	}
	//어코드 수정
	@PutMapping("/{accordNo}")
	public void updateAccord(@PathVariable long accordNo, @RequestBody AccordDto accordDto) {
		accordDto.setAccordNo(accordNo);
		accordDao.update(accordDto);
	}
	//어코드 삭제
	@DeleteMapping("/{accordNo}")
	public void deleteAccord(@PathVariable int accordNo) {
		accordDao.delete(accordNo);
	}
	
	
	
}
