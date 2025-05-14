package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.ManagementDao;
import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dao.kakaopay.PayDao;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.dto.kakaopay.PayDetailDto;
import com.hiki.academyfinal.dto.kakaopay.PayDto;
import com.hiki.academyfinal.vo.AdminPageVO;
import com.hiki.academyfinal.vo.ManagementResponseVO;
import com.hiki.academyfinal.vo.UsersListResponseVO;

@CrossOrigin
@RestController
@RequestMapping("/api/management")
public class ManagementRestController {
	
	@Autowired
	private ManagementDao managementDao;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private PayDao payDao;
	
	@PostMapping
	public UsersListResponseVO usersList(@RequestBody AdminPageVO adminPageVO){
		//나중에 추가  아이거 또 코드길어지네
//		if(adminPageVO.getUsersType().equals("관리자") == false) {
//			throw new TargetNotFoundException("나가세요");
//		}
		int count = managementDao.count();
		List<ManagementResponseVO> list = managementDao.usersList(adminPageVO);
		UsersListResponseVO usersListResponseVO = new UsersListResponseVO();
		usersListResponseVO.setCount(count);
		usersListResponseVO.setUsersList(list);
		System.out.println(usersListResponseVO);
		return usersListResponseVO;
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> upaateUsers(@RequestBody UsersDto usersDto){
		usersDao.updateAll(usersDto);
		return ResponseEntity.ok("업데이트완료");
	}
	
	@GetMapping("/usersPay/{usersId}")
	public List<PayDto> usersPay(@PathVariable String usersId) {
		return payDao.findUsersPay(usersId);
	}
	
	@GetMapping("/payDetail/{payNo}")
	public List<PayDetailDto> findUsersPayDetail(@PathVariable long payNo) {
		return payDao.findUsersPayDetail(payNo);
	}
}
