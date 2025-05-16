package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.UsersDto;
import com.hiki.academyfinal.error.TargetNotFoundException;
import com.hiki.academyfinal.vo.UsersLoginVO;

@Repository
public class UsersDao {

	@Autowired 
	private SqlSession sqlSession;
	
	@Autowired
	private PasswordEncoder encoder;
	
	//회원가입 + 비밀번호 암호화
	public void insert(UsersDto usersDto) {
//		 System.out.println("usersId: " + usersDto.getUsersId()); 
		String pwEncoder = encoder.encode(usersDto.getUsersPw());
		usersDto.setUsersPw(pwEncoder);

		sqlSession.insert("users.join",usersDto);
	}
	public void insertNaver(UsersDto usersDto) {
		sqlSession.insert("users.naverJoin",usersDto);
	}
	
	//아이디중복검사 // 조회
	public UsersDto findId(String usersId) {
		return sqlSession.selectOne("users.findId", usersId);
	}
	public UsersDto findId(UsersDto usersDto) {
		return sqlSession.selectOne("users.findId", usersDto);
	}
	
	//로그인
	public UsersDto login(UsersDto usersDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UsersDto findDto = findId(usersDto.getUsersId());
		if(findDto == null) return null;
		boolean isValid = encoder.matches(usersDto.getUsersPw(), findDto.getUsersPw());
		
		System.out.println(isValid);
		return isValid ? findDto : null;
	}
	
	public UsersDto selectOne(String usersId) {
		return sqlSession.selectOne("users.find", usersId);
	}
	public UsersDto selectOne(UsersDto usersDto) {
		return sqlSession.selectOne("users.find", usersDto);
	}
	
	//이메일찾기
	public String findEmail(String usersId) {
		return sqlSession.selectOne("users.findEmail", usersId);
	}
	
	//비밀번호수정
	public boolean updatePw(Map<String, Object> usersIdPw) {
	    String usersId = (String) usersIdPw.get("usersId");
	    String rawPw = (String) usersIdPw.get("usersPw");

	    if (usersId == null || rawPw == null) {
	        throw new IllegalArgumentException("ID 또는 비밀번호가 null입니다.");
	    }

	    String pwEncoder = encoder.encode(rawPw);

	    Map<String, Object> result = new HashMap<>();
	    result.put("usersId", usersId);
	    result.put("usersPw", pwEncoder);

	    return sqlSession.update("users.updatePw", result) > 0;
	}
	//비밀번호 수정 다르게..
	public boolean updatePw(UsersLoginVO usersLoginVO) {
		 if (usersLoginVO.getUsersId().isEmpty() || usersLoginVO.getUsersPw().isEmpty() ||
				 usersLoginVO.getUsersId() == null || usersLoginVO.getUsersPw() == null) {
		        throw new IllegalArgumentException("ID 또는 비밀번호가 null입니다.");
		    }
		 String pwEncoder = encoder.encode(usersLoginVO.getUsersPw());
		 usersLoginVO.setUsersPw(pwEncoder);
		 return sqlSession.update("users.updatePw",usersLoginVO) >0;
	}
	
	//업데이트(전체)
	public boolean updateAll(UsersDto usersDto) {
		return sqlSession.update("users.update", usersDto) >0;
	}
	//회원탈퇴
	public boolean exit(UsersDto usersDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UsersDto findDto = findId(usersDto.getUsersId());
		if(findDto == null) {
			throw new TargetNotFoundException("잘못된 회원정보");
		}
		boolean isValid = encoder.matches(usersDto.getUsersPw(), findDto.getUsersPw());
		if(isValid) {
			System.out.println("입력 비밀번호: " + usersDto.getUsersPw());
			System.out.println("DB 비밀번호: " + findDto.getUsersPw());
			System.out.println("검증결과: " + encoder.matches(usersDto.getUsersPw(), findDto.getUsersPw()));
			return sqlSession.delete("users.delete",usersDto.getUsersId())>0;
		}
		throw new TargetNotFoundException("비번틀림");
	}
	//네이버유저 걍지움
	public boolean exitNaver(String usersId) {
		return sqlSession.delete("users.delete",usersId)>0;
	}
}
