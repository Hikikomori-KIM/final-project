package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersRestController {
	
	@Autowired
	private UsersDao usersDao;
	
	//가입
	@PostMapping("/")
	public void join(@RequestBody UsersDto usersDto) {
		
		System.out.println(usersDto.getUsersContact());
		usersDao.insert(usersDto);
	}
	//아이디중복
	@GetMapping("/usersId/{usersId}")
	public void overlapId(@PathVariable String usersId) {
	 UsersDto usersDto = usersDao.findId(usersId);
	 if(usersDto == null) { //null 이면 404 (가입가능)
		 throw new TargetNotFoundException();
	 	}
	}
}
