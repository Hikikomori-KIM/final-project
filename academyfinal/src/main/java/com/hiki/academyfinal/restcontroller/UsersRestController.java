package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dto.UsersDto;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
	
	@Autowired
	private UsersDao usersDao;
	
	@PostMapping("/")
	public void join(@RequestBody UsersDto usersDto) {
		usersDao.insert(usersDto);
	}
	
}
