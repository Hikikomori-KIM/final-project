package com.hiki.academyfinal.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiki.academyfinal.dao.MessageDao;
import com.hiki.academyfinal.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/service/contact")
public class MessageRestController {
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private TokenService tokenService;
	
	@GetMapping("/")
	
}
