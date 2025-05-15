package com.hiki.academyfinal.dao.kakaopay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hiki.academyfinal.dto.kakaopay.PayDetailDto;
import com.hiki.academyfinal.dto.kakaopay.PayDto;
import com.hiki.academyfinal.dto.kakaopay.PurchaseDetailViewDto;
import com.hiki.academyfinal.vo.DeliveryRequestVO;
import com.hiki.academyfinal.vo.DeliveryResponseVO;
import com.hiki.academyfinal.vo.kakaopay.PayTotalVO;

@Repository
public class PayDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public long addPay(PayDto payDto) {
		long payNo = sqlSession.selectOne("pay.paySequence");
		payDto.setPayNo(payNo);
		sqlSession.insert("pay.addPay", payDto);
		return payNo;
	}
	public long addPayDetail(PayDetailDto payDetailDto) {
		long payDetailNo = sqlSession.selectOne("pay.payDetailSequence");
		payDetailDto.setPayDetailNo(payDetailNo);
		sqlSession.insert("pay.addPayDetail", payDetailDto);
		return payDetailNo;
	}
	
	public List<PayDto> listPay() {
		return sqlSession.selectList("pay.listPay");
	}
	public List<PayDetailDto> listPayDetail(long payDetailOrigin) {
		return sqlSession.selectList("pay.listPayDetail", payDetailOrigin);
	}
	
	public List<PayTotalVO> listTotalManual() {
		List<PayTotalVO> results = new ArrayList<>();
		
		List<PayDto> list = this.listPay();//[1]
		for(PayDto payDto : list) {//[2]
			List<PayDetailDto> details = this.listPayDetail(payDto.getPayNo());
			
			PayTotalVO vo = new PayTotalVO();//[3]
			vo.setPayDto(payDto);
			vo.setPayList(details);
			
			results.add(vo);
		}
		return results;
	}
	public List<PayTotalVO> listTotalAuto() {
		return sqlSession.selectList("pay.listPayAuto");
	}
	public List<PayTotalVO> listTotalAuto(String userId) {
		return sqlSession.selectList("pay.listPayAuto", userId);
	}
	
	public PayDto selectOne(long payNo) {
		return sqlSession.selectOne("pay.findPay", payNo);
	}
	
	public PayDetailDto selectDetailOne(long payDetailNo) {
		return sqlSession.selectOne("pay.findDetail", payDetailNo);
	}
	
	// 결제 취소
	public boolean cancelAll(long payNo) {
		return sqlSession.update("pay.cancelAll", payNo) > 0;
	}
	public boolean updatePay(long payNo, long payRemain) {
		Map<String, Object> params = new HashMap<>();
		params.put("payNo", payNo);
		params.put("payRemain", payRemain);
		return sqlSession.update("pay.updatePay", params) > 0;
	}
	
	// 배송 과정 변경 기능 (관리자용)
	public String findDeliveryStatus(long payNo) {
		return sqlSession.selectOne("pay.findDeliveryStatus", payNo);
	}
	
	public void updateDeliveryStatus(long payNo, String newStatus) {
		Map<String, Object> params = new HashMap<>();
		params.put("payNo", payNo);
		params.put("status", newStatus);
		sqlSession.update("pay.updateDeliveryStatus", params);
	}
	// 전체 결제 목록 조회(관리자용)
	public List<Map<String, Object>> findDeliveryList() {
		return sqlSession.selectList("pay.findDeliveryList");
	}
	
	// 구매목록 조회 (결제 결과 출력)
	public List<PayDto> getPurchaseListByUserId(String usersId) {
		return sqlSession.selectList("pay.selectListByUserId", usersId);
	}
	
	// 구매목록 → 상세사항
	public List<PurchaseDetailViewDto> selectPayDetailList(long payNo) {
	    return sqlSession.selectList("pay.selectPayDetailList", payNo);
	}
	
	//유저 구매조회
	public List<PayDto> findUsersPay(String usersId) {
		return sqlSession.selectList("pay.findUserPay", usersId);
	}
	
	//구매목록디테일조회
	public List<PayDetailDto> findUsersPayDetail(long payOriginNum) {
		return sqlSession.selectList("pay.findUsersDetailPay", payOriginNum);
	}
	//해당 유저가 특정 상품을 구매했는지 확인
	public boolean hasUserPurchasedProduct(String usersId,long productNo) {
		Map<String,Object> params = new HashMap<>();
		params.put("usersId", usersId);
		params.put("productNo", productNo);
		int count = sqlSession.selectOne("pay.hasUserPurchasedProduct",params);
		return count>0;
	}
	
	public List<PayDto> selectAllPayList() {
		return sqlSession.selectList("pay.selectAll");
	}
	
	
	//정정히Dao침투하기
	//모든 유저의 구매목록 + search+ 판매까지 
	public DeliveryResponseVO payAllList(DeliveryRequestVO deliveryRequestVO){
		List<PayDto> list = sqlSession.selectList("delivery.listAll",deliveryRequestVO);
		Long totalPrice = sqlSession.selectOne("delivery.totalPrice", deliveryRequestVO);
		int count = sqlSession.selectOne("delivery.count", deliveryRequestVO);
		DeliveryResponseVO deliveryResponseVO = new DeliveryResponseVO();
		deliveryResponseVO.setList(list);
		deliveryResponseVO.setTotalPrice(totalPrice);
		deliveryResponseVO.setCount(count);
		return deliveryResponseVO;
	}
	
	
	//배송중으로변경
	public boolean shippingUpdate(long payNo){
		return sqlSession.update("delivery.onDelivery",payNo) >0;
	}
	//배송완료로 변경
	public boolean complete(long payNo){
		return sqlSession.update("delivery.finish",payNo) >0;
	}
	
	//중요 배송중인지아닌지 판별해야함 여기서 그냥 배송중이면 true 아니면 false쳐버림
	public boolean findShipping(long payNo){
		String ddd = sqlSession.selectOne("delivery.findShipping",payNo);
		if(ddd.equals("배송중")) {
			return true;
		}
		return false;
	}
	
	//반품요청중으로 변경
	public boolean returnProduct(long payNo){
		return sqlSession.update("delivery.returnProduct",payNo) >0 ; 
	}
	
	//반품완료로 변경
	public boolean returnComplete(long payNo){
		return sqlSession.update("delivery.returnComplete",payNo) >0;
	}
	
	public boolean updateShippingStatus(long payNo, String newStatus) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("payNo", payNo);
	    params.put("newStatus", newStatus);
	    return sqlSession.update("pay.updateShippingStatus", params) > 0;
	}


	
}