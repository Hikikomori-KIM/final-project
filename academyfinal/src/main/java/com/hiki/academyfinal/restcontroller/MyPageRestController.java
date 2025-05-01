package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.AddressListDao;
import com.hiki.academyfinal.dao.UsersDao;
import com.hiki.academyfinal.dto.AddressListDto;
import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.vo.MyPageVO;

@CrossOrigin
@RestController
@RequestMapping("/api/mypage")
public class MyPageRestController {
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private AddressListDao addressListDao;
	
	//유저정보조회 (본인)
	@GetMapping("/{usersId}")
	public MyPageVO myPage(@PathVariable String usersId) {
		UsersDto usersDto = usersDao.findId(usersId);
		if(usersDto == null) throw new TargetNotFoundException("존재하지 않는 유저");
		AddressListDto findMainAddress = addressListDao.findMainAddress(usersId);
		
		MyPageVO.MyPageVOBuilder builder = MyPageVO.builder()
			    .usersName(usersDto.getUsersName())
			    .usersContact(usersDto.getUsersContact())
			    .usersBirth(usersDto.getUsersBirth())
			    .usersEmail(usersDto.getUsersEmail());
		//대표 주소있는 사람만 주소보내줌 없는사람 null
			if (findMainAddress != null) {
			    builder.addressListBasic(findMainAddress.getAddressListBasic())
			           .addressListDetail(findMainAddress.getAddressListDetail());
			}
			return builder.build();
	}

	//주소리스트조회 (전체)
	@GetMapping("/address/{usersId}")
	public List<AddressListDto> addressList(@PathVariable String usersId) {
		List<AddressListDto> addressListDtoList = addressListDao.myAddressList(usersId);
		return addressListDtoList;
	}
}
