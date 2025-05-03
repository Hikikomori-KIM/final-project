package com.hiki.academyfinal.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.CartDao;
import com.hiki.academyfinal.dto.CartDto;
import com.hiki.academyfinal.error.TargetNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartRestController {

}
