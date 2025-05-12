package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.kakaopay.PayDao;

@RestController
@RequestMapping("/api/admin/delivery")
@CrossOrigin
public class AdminDeliveryRestController {

    @Autowired
    private PayDao payDao;

    // 배송 상태 조회
    @GetMapping("/{payNo}")
    public String getDeliveryStatus(@PathVariable long payNo) {
        return payDao.findDeliveryStatus(payNo);
    }

    // 배송 상태 변경
    @PatchMapping("/{payNo}")
    public void updateDeliveryStatus(@PathVariable long payNo, @RequestBody String newStatus) {
        payDao.updateDeliveryStatus(payNo, newStatus);
    }
}

