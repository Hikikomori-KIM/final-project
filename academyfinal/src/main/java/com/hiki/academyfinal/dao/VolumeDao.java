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
	
	public int sequence() {
		return sqlSession.selectOne("volume.sequence");
	}
	
	public void insert(VolumeDto volumeDto) {
		sqlSession.insert("volume.add",volumeDto);
	}

	public void deleteByProductNo(int productNo) {
		sqlSession.delete("volume.deleteByProductNo",productNo);
	}
	public List<VolumeDto> selectVolumes(int productNo) {
	    return sqlSession.selectList("volume.selectVolumes", productNo);
	}
	public void updateVolume(VolumeDto volumeDto) {
		sqlSession.update("volume.updateVolume", volumeDto);
	}
}
