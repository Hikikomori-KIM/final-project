package com.hiki.academyfinal;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class uuidTest {

	@Test
	public void uuid() {
	    String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 15); //번호만
	    String fakeTid = "Cash_" + uuidPart; //char20맞춤
	    log.debug("uuid : " + fakeTid);
	}

}
