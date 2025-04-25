package com.hiki.academyfinal.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.VolumeDto;

@Repository
public class VolumeDao {
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("volume.squence");
	}
	
	public void insert(VolumeDto volumeDto) {
		sqlSession.insert("volume.add",volumeDto);
	}
}
