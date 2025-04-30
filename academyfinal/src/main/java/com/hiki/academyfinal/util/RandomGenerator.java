package com.hiki.academyfinal.util;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {
	Random r = new Random();
	
	Format f = new DecimalFormat("000000");
	
	public String randomNumber(int size) {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<size; i++) {		
			buffer.append(r.nextInt(10));
		}
		return buffer.toString();
	}

//랜덤문자열생성기 비밀번호 줄꺼
    public String randomString() {
        String aaaa = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        Random r = new Random();
        int length = aaaa.length();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 10; i++) {  
           randomString.append(aaaa.charAt(r.nextInt(length)));
    }
        return randomString.toString(); 
    }
}
