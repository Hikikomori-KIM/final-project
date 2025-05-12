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
	// ✅ 기존 용량들 soft delete (volume_status = 'deleted')
	public void markDeletedByProductNo(long productNo) {
	    sqlSession.update("volume.markDeletedByProductNo", productNo);
	}

	// ✅ product_no와 volume_ml로 기존 데이터 존재 여부 확인
	public VolumeDto findByProductNoAndMl(long productNo, String volumeMl) {
	    return sqlSession.selectOne("volume.findByProductNoAndMl", 
	        new java.util.HashMap<String, Object>() {{
	            put("productNo", productNo);
	            put("volumeMl", volumeMl);
	        }}
	    );
	}

	// ✅ volume 상태값 변경
	public void updateVolumeStatus(long volumeNo, String status) {
	    sqlSession.update("volume.updateVolumeStatus", 
	        new java.util.HashMap<String, Object>() {{
	            put("volumeNo", volumeNo);
	            put("status", status);
	        }}
	    );
	}
	
	// volumeNo로 조회하는 메서드 (결제 시 필요)
	public VolumeDto selectByVolumeNo(long volumeNo) {
	    return sqlSession.selectOne("volume.selectByVolumeNo", volumeNo);
	}
	
	// 결제용 조인 쿼리 (결제 후 정보를 DB에 넘기는 용도)__productName, productPrice까지 조인
	public VolumeDto selectWithProductByVolumeNo(long volumeNo) {
		return sqlSession.selectOne("volume.selectWithProductByVolumeNo", volumeNo);
	}

}
