package com.hiki.academyfinal.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
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

	//주소리스트조회 (전체) + 카운트추가
	@GetMapping("/address/{usersId}")
	public Map<String,Object> addressList(@PathVariable String usersId) {
		Map<String,Object>result = new HashMap<>();
		List<AddressListDto> addressListDtoList = addressListDao.myAddressList(usersId);
		int count = addressListDao.listCount(usersId);
		result.put("addressListDtoList", addressListDtoList);
		result.put("count", count);
		
		return result;
	}
	
	//본인정보수정
	@PostMapping("/{usersId}")
	public UsersDto update(@RequestBody UsersDto usersDto) {
		UsersDto findDto = usersDao.findId(usersDto.getUsersId());
		usersDao.updateAll(usersDto);
		findDto.setUsersPw(null); //혹시몰라서비번 없앰
		return findDto;
	}
	
	//주소1개삭제
	@DeleteMapping("/address/{addressListNo}")
	public ResponseEntity<String> delete(@PathVariable long addressListNo) {
		addressListDao.delete(addressListNo);
		return ResponseEntity.ok("삭제완료");
	}
	
	//메인주소가 있다면 메인주소 기본으로돌리고 요청한주소 메인으로변경
	@PostMapping("/mainAddress/{addressListNo}")
	public ResponseEntity<String> mainUpdate(@PathVariable long addressListNo ,  @RequestBody Map<String, String> requestData){
		//메인주소 찾음
		String usersId = requestData.get("usersId");
		AddressListDto findMain = addressListDao.findMainAddress(usersId);
//		System.out.println("유저아이디넘어오나" +usersId);
//		System.out.println("메인주소 찾기 " +findMain);
//		System.out.println("바꿀 주소 " +addressListNo);
		//메인주소없으면 그냥 메인으로 올려줌 
		if(findMain == null) {
			addressListDao.updateMain(addressListNo);
			return ResponseEntity.ok("메인주소 등록완료");
		}
		else{
			//기본주소다운그레이드
			addressListDao.updateCommon(findMain.getAddressListNo());
			addressListDao.updateMain(addressListNo);
			return ResponseEntity.ok("메인주소 변경!완료");
		}
	}
	//주소추가
	@PostMapping("/insertAddress")
	public void insertAddress(@RequestBody AddressListDto addressListDto) {
//		System.out.println(addressListDto);
		int count = addressListDao.listCount(addressListDto.getUsersId());
		if(count >=5) throw new TargetNotFoundException("주소개수초과") ;
		addressListDto.setAddressListDefault("N");
		addressListDao.insert(addressListDto);
	} 
	//주소수정
	@PostMapping("/updateAllAddress")
	public ResponseEntity<String> updataAddress(@RequestBody AddressListDto addressListDto){
		ModelMapper mapper = new ModelMapper();
		AddressListDto addressDto = mapper.map(addressListDto, AddressListDto.class);
		addressListDao.update(addressDto);
		System.out.println(addressDto);
		return ResponseEntity.ok("주소변경완료");
	}
	
}
