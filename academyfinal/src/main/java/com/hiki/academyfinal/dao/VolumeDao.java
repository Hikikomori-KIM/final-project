package com.hiki.academyfinal.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.VolumeDto;

@Repository
public class VolumeDao {
	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("volume.sequence");
	}
	
	public void insert(VolumeDto volumeDto) {
		sqlSession.insert("volume.add",volumeDto);
	}

	public void deleteByProductNo(long productNo) {
		sqlSession.delete("volume.deleteByProductNo",productNo);
	}
	public List<VolumeDto> selectVolumes(long productNo) {
	    return sqlSession.selectList("volume.selectVolumes", productNo);
	}
	public void updateVolume(VolumeDto volumeDto) {
		sqlSession.update("volume.updateVolume", volumeDto);
	}
	//특정 상품번호에 연결된 기존의 모든 용량들을 가져오는 메서드
	public List<VolumeDto> selectByProductNo(long productNo) {
	    return sqlSession.selectList("volume.selectByProductNo", productNo);
	}
}
