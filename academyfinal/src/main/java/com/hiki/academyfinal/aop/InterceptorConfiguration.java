package com.hiki.academyfinal.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class InterceptorConfiguration implements WebMvcConfigurer{

	@Autowired
	private LoginInterceptor loginInterceptor;

	//aop 미룸 
//	public void addInterceptors(InterceptorRegistry registry) {
	//토큰리뉴얼 
//		registry.addInterceptor(tokenRenewalInterceptor) 
//		.addPathPatterns("/**");
	//로그인차단
//		registry.addInterceptor(loginInterceptor)
//		.addPathPatterns("/api/**");
	}

