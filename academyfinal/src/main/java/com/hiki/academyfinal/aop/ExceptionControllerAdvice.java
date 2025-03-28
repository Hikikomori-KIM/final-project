package com.hiki.academyfinal.aop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hiki.academyfinal.error.TargetNotFoundException;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionControllerAdvice {
	@ExceptionHandler(TargetNotFoundException.class)
	public ResponseEntity<String> notFound() {
		return ResponseEntity.notFound().build();
		// return ResponseEntity.status(404).build();
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> error(Exception e) {
		e.printStackTrace();
		return ResponseEntity.internalServerError().build();
	}
}
