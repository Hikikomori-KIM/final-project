package com.hiki.academyfinal.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	//카카오페이 결제 관련 용량이랑 상품번호(기존코드)
//	public VolumeDto selectOne(long volumeNo, long productNo) { 
//	    return sqlSession.selectOne("volume.selectOne", 
//	        new java.util.HashMap<String, Object>() {{
//	            put("volumeNo", volumeNo);
//	            put("productNo", productNo);
//	        }}
//	    );
//	}
	//경고 해결버전
	//기존 java.util.HashMap<String, Object>() 은 익명 내부 클래스를 생성하는데 
	//자바 컴파일러는 익명 클래스도 직렬화 할 수 있기 때문에 Serializable 을 암묵적으로 상속받는 구조로 가눚하고 경고를 띄우는 경우가 많음
	// 그래서 serializable class does not declare a serialVersionUID 경고가 뜸 
	// 익명 클래스 대신 명시적 map 사용해서 경고 해결 버전 
	public VolumeDto selectOne(long volumeNo, long productNo) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("volumeNo", volumeNo);
	    param.put("productNo", productNo);
	    return sqlSession.selectOne("volume.selectOne", param);
	}

	
	
}
