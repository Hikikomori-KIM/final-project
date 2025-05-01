package com.hiki.academyfinal.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	//본인정보수정
	@PostMapping("/{usersId}")
	public void update(@RequestBody UsersDto usersDto) {
		UsersDto findDto = usersDao.findId(usersDto.getUsersId());
		usersDao.updateAll(usersDto);
		findDto.setUsersPw(null); //혹시몰라서비번 없앰
	}
	
	//주소1개삭제
	@DeleteMapping("/address/{addressListNo}")
	public ResponseEntity<String> delete(@PathVariable long addressListNo) {
		addressListDao.delete(addressListNo);
		return ResponseEntity.ok("삭제완료");
	}
	
	//메인주소가 있다면 메인주소 기본으로돌리고 아닌주소 메인ㄴ으로변경
//	@PostMapping("/address")낼해야지졸료
}
