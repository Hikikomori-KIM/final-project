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

}
