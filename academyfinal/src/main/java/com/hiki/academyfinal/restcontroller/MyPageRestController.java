package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/mypage")
public class MyPageRestController {
	@Autowired
	private UsersDao usersDao;
	
	//유저정보조회
	@GetMapping("/{usersId}")
	public UsersDto myPage(@PathVariable String usersId) {
		UsersDto usersDto = usersDao.findId(usersId);
		if(usersDto == null) throw new TargetNotFoundException("존재하지 않는 유저");
		usersDto.setUsersPw(null);
		return usersDto;
	}

}
