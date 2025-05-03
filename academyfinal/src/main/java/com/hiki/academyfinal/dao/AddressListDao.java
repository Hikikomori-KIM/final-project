package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.AddressListDto;

@Repository
public class AddressListDao {
@Autowired
private SqlSession sqlSession;
	
	//등록
	public void insert(AddressListDto addressListDto) {
		long no = sqlSession.selectOne("addressList.sequence");
		addressListDto.setAddressListNo(no);
		sqlSession.insert("addressList.insert",addressListDto);
		}
	
	//업데이트
	public boolean update(AddressListDto addressListDto) {
		return sqlSession.update("addressList.update",addressListDto) >0;
	}
	
	//내주소 조회
	public List<AddressListDto> myAddressList(String usersId){
		return sqlSession.selectList("addressList.selectAddressList",usersId);
	}
	
	//대표주소있나 조회
	public AddressListDto findMainAddress(String usersId) {
		return sqlSession.selectOne("addressList.selectMain",usersId);
	}
	
	//메인주소를 일반주소로 바꿈
	public boolean updateCommon(long addressListNo) {
		return sqlSession.update("addressList.updateCommon", addressListNo)>0;
	}

	//메인주소로 업뎃
	public boolean updateMain(long addressListNo) {
		return sqlSession.update("addressList.updateMain", addressListNo)>0;
	}
	
	//그냥주소한개삭제
	public boolean delete(long addressListNo) {
		return sqlSession.delete("addressList.deleteOne",addressListNo)>0;
	}
	
	//그냥 단일조회1
	public AddressListDto findOne(long addressListNo) {
	    return sqlSession.selectOne("addressList.selectOne", addressListNo);
	}
	
	//카운트
	public int listCount(String usersId) {
		return sqlSession.selectOne("addressList.count",usersId);
	}
}
